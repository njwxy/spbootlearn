package com.wxy.EnergyEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EnergyVersion {
    public long devAddr;
    public short module;
    public String version;
}
