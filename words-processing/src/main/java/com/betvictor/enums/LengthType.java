package com.betvictor.enums;


import lombok.Getter;

@Getter
public enum LengthType {
  SHORT("short"),
  MEDIUM("medium"),
  LONG("long"),
  VERYLONG("verylong");

  private final String type;

  LengthType(String type) {
    this.type = type;
  }
}
