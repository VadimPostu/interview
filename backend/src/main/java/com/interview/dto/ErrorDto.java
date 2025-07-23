package com.interview.dto;

import java.io.Serializable;

public record ErrorDto(String errorCode, String errorMessage) implements Serializable {

}
