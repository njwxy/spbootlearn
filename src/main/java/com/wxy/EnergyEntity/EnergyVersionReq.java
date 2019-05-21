package com.wxy.EnergyEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EnergyVersionReq {
    public long devAddr;
    public short module; /* 0:通讯模块 1:采集模块 */
}
