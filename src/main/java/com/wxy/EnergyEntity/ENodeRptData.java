package com.wxy.EnergyEntity;

import javolution.io.Struct;

import java.util.Date;

public class ENodeRptData extends Struct {

    private final static int PHASE_A = 0x0;
    private final static short PHASE_B = 0x1;
    private final static short PHASE_C = 0x2;
    private final static short PHASE_COM = 0x3;
 /*
	u16 avolt;
	u16 bvolt;
	u16 cvolt;
	u32 aCurrent;
	u32 bCurrent;
	u32 cCurrent;
	u16 freq;
	u32 aAp; 		  power
    u32 bAp;
    u32 cAp;
    u32 aPosAe;  	 **a positive energy *
     u32 bPosAe;
    u32 cPosAe;
    u32 comAe; 		 *combinate energy*
    u8  comState;    * 0:online 1:offline *
*/

    public final Unsigned16 avolt = new Unsigned16();
    public final Unsigned16 bvolt = new Unsigned16();
    public final Unsigned16 cvolt = new Unsigned16();

    public final Unsigned32 aCurrent = new Unsigned32();
    public final Unsigned32 bCurrent = new Unsigned32();
    public final Unsigned32 cCurrent = new Unsigned32();

    public final Unsigned16 freq = new Unsigned16();

    public final Unsigned32 aAp = new Unsigned32();
    public final Unsigned32 bAp = new Unsigned32();
    public final Unsigned32 cAp = new Unsigned32();


    public final Unsigned32 aPosAe = new Unsigned32();
    public final Unsigned32 bPosAe = new Unsigned32();
    public final Unsigned32 cPosAe = new Unsigned32();
    public final Unsigned32 comAe = new Unsigned32();
    public final Unsigned8 comState = new Unsigned8();
    public final Unsigned8 relayState = new Unsigned8();

    public Date time;

    float getVolt(int phase)
    {
        float voltRet=0;
        switch (phase)
        {
            case PHASE_A:
                voltRet = avolt.get();
                break;
            case PHASE_B:
                voltRet = bvolt.get();
                break;
            case PHASE_C:
                voltRet = cvolt.get();
                break;
        }
        voltRet = voltRet/10;
        return voltRet;
    }

    float getCurrent(int phase)
    {
        float currentRet=0;
        switch (phase)
        {
            case PHASE_A:
                currentRet = aCurrent.get();
                break;
            case PHASE_B:
                currentRet = bCurrent.get();
                break;
            case PHASE_C:
                currentRet = cCurrent.get();
                break;
        }
        currentRet = currentRet/100;
        return currentRet;
    }

    float getFreq()
    {
        float freqret=0;
        freqret = freq.get();
        freqret = freqret/100;
        return freqret;
    }


    float getPower(int phase)  /* positive power */
    {
        float powerRet = 0;
        switch (phase)
        {
            case PHASE_A:
                powerRet = aAp.get();
                break;
            case PHASE_B:
                powerRet = bAp.get();
                break;
            case PHASE_C:
                powerRet = cAp.get();
                break;
        }
        return powerRet/1000;
    }

    float getEnergy(int phase)  /* positive power */
    {
        float energyRet = 0;
        switch (phase)
        {
            case PHASE_A:
                energyRet = aPosAe.get();
                break;
            case PHASE_B:
                energyRet = bPosAe.get();
                break;
            case PHASE_C:
                energyRet = cPosAe.get();
                break;
            case PHASE_COM:
                energyRet = comAe.get();
                break;


        }
        return energyRet/1000;
    }

    int enodeState()
    {
        return comState.get(); /*0 or 1*/
    }

    int getRelayState()
    {
        return relayState.get();
    }

}
