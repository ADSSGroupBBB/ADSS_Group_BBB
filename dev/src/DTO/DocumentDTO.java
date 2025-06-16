package DTO;

import java.util.List;

public record DocumentDTO(String docID, List<ShipmentItemDTO> items, String date, String truck_id,
                          String dep_hour, String driver_id, String dep_from, List<LocationDTO> destinations, String eventMessage) {}