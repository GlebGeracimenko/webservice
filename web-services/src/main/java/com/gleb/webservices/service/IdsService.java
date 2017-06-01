package com.gleb.webservices.service;

import java.util.List;
import java.util.UUID;

public interface IdsService {
    UUID getNewId();
    String getNewIdAsString();
    UUID convert(String id);
    List<UUID> convert(List<String> ids);
}
