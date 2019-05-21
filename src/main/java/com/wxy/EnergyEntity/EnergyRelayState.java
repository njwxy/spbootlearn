package com.wxy.EnergyEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@AllArgsConstructor
public class EnergyRelayState {
    public long devAddr;
    public short state;  /* 0:关断 1:闭合 */

}
