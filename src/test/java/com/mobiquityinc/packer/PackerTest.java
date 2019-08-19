package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class PackerTest {

    @Test
    void packTest() {
        /* negative cases */
        assertThrows(APIException.class,  () -> Packer.pack(null));
        assertThrows(APIException.class,  () -> Packer.pack(""));
        assertThrows(APIException.class,  () -> Packer.pack("///"));
    }
}
