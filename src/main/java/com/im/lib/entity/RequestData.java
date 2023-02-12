package com.im.lib.entity;

import lombok.Data;

import java.math.BigInteger;

@Data
public class RequestData {

    private BigInteger authKeyId;

    private Object requestParam;

    private int constructorId;

    private String salt;

    private long sessionId;

    private BigInteger msgId;

    private String seqNo;

    private byte[] data;
}
