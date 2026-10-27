package com.foodrush.api.service;

import com.foodrush.api.dto.request.AddressRequestDTO;
import com.foodrush.api.exception.BusinessException;
import com.foodrush.api.model.Address;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.core.ParameterizedTypeReference;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class GeocodingService {

  private final RestClient restClient;

  public GeocodingService() {
    this.restClient = RestClient.builder()
      .defaultHeader("User-Agent", "FoodRushAPI/1.0")
      .build();
  }

  public GeocodingService(RestClient restClient) {
    this.restClient = restClient;
  }

  public Address resolveAddress(AddressRequestDTO dto) {
    String cleanCep = dto.cep().replaceAll("\\D", "");

    BrasilApiCepResponse cepData = fetchCepFromBrasilApi(cleanCep);

    BigDecimal latitude = null;
    BigDecimal longitude = null;

    if (
      cepData.location() != null &&
      cepData.location().coordinates() != null &&
      cepData.location().coordinates().latitude() != null &&
      cepData.location().coordinates().longitude() != null &&
      !cepData.location().coordinates().latitude().isBlank() &&
      !cepData.location().coordinates().longitude().isBlank()
    ) {
      latitude = new BigDecimal(
        cepData.location().coordinates().latitude()
      ).setScale(6, RoundingMode.HALF_UP);
      longitude = new BigDecimal(
        cepData.location().coordinates().longitude()
      ).setScale(6, RoundingMode.HALF_UP);
    } else {
      BigDecimal[] coords = fetchCoordinatesFromNominatim(
        cepData.street(),
        dto.number(),
        cepData.city(),
        cepData.state()
      );
      if (coords != null) {
        latitude = coords[0];
        longitude = coords[1];
      } else {
        latitude = BigDecimal.ZERO.setScale(6, RoundingMode.HALF_UP);
        longitude = BigDecimal.ZERO.setScale(6, RoundingMode.HALF_UP);
      }
    }

    return Address.builder()
      .cep(cleanCep)
      .street(cepData.street() != null ? cepData.street() : "N/A")
      .number(dto.number())
      .complement(dto.complement())
      .neighborhood(
        cepData.neighborhood() != null ? cepData.neighborhood() : "Centro"
      )
      .city(cepData.city())
      .state(cepData.state())
      .latitude(latitude)
      .longitude(longitude)
      .build();
  }

  private BrasilApiCepResponse fetchCepFromBrasilApi(String cleanCep) {
    try {
      BrasilApiCepResponse response = restClient
        .get()
        .uri("https://brasilapi.com.br/api/cep/v2/{cep}", cleanCep)
        .retrieve()
        .body(BrasilApiCepResponse.class);

      if (response == null || response.city() == null) {
        throw new BusinessException(
          "CEP não encontrado ou inválido: " + cleanCep
        );
      }
      return response;
    } catch (RestClientException e) {
      throw new BusinessException(
        "Não foi possível localizar o CEP informado: " + cleanCep
      );
    }
  }

  private BigDecimal[] fetchCoordinatesFromNominatim(
    String street,
    String number,
    String city,
    String state
  ) {
    try {
      String query = String.format(
        "%s, %s, %s, %s, Brazil",
        street != null ? street : "",
        number != null ? number : "",
        city != null ? city : "",
        state != null ? state : ""
      );

      List<Map<String, Object>> results = restClient
        .get()
        .uri(
          "https://nominatim.openstreetmap.org/search?format=json&limit=1&q={q}",
          query
        )
        .retrieve()
        .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

      if (results != null && !results.isEmpty()) {
        Map<String, Object> first = results.getFirst();
        Object latObj = first.get("lat");
        Object lonObj = first.get("lon");
        if (latObj != null && lonObj != null) {
          return new BigDecimal[] {
            new BigDecimal(latObj.toString()).setScale(6, RoundingMode.HALF_UP),
            new BigDecimal(lonObj.toString()).setScale(6, RoundingMode.HALF_UP),
          };
        }
      }
    } catch (Exception ignored) {
      // Fallback silencioso caso o serviço secundário de geocoding oscile
    }
    return null;
  }

  public record BrasilApiCepResponse(
    String cep,
    String state,
    String city,
    String neighborhood,
    String street,
    LocationData location
  ) {}

  public record LocationData(String type, CoordinatesData coordinates) {}

  public record CoordinatesData(String longitude, String latitude) {}
}

