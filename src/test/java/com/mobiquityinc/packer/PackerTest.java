package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.pojo.Case;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PackerTest {

    @Test
    void packTest() {
        /* negative cases */
        assertThrows(APIException.class,  () -> Packer.pack(null));
        assertThrows(APIException.class,  () -> Packer.pack(""));
        assertThrows(APIException.class,  () -> Packer.pack("///"));
    }

    @Test
    void parseItemsTest() throws APIException {
        Case c1 = Packer.parseCase("81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)");
        assertEquals(81, c1.getWeightLimit());
        assertEquals(6, c1.getItems().size());
        assertEquals(1, c1.getItems().get(0).getIndex());
        assertEquals(88.62f, c1.getItems().get(1).getWeight());
        assertEquals(3, c1.getItems().get(2).getCost());

        Case c2 = Packer.parseCase("100 :");
        assertEquals(100, c2.getWeightLimit());
        assertEquals(0, c2.getItems().size());

        assertThrows(APIException.class, () -> Packer.parseCase("100 : ()"));
        assertThrows(APIException.class, () -> Packer.parseCase(" : (1,53.38,€45)"));
        assertThrows(APIException.class, () -> Packer.parseCase("100 : (1,53.38,€45))"));
        assertThrows(APIException.class, () -> Packer.parseCase("100 (1,53.38,€45)"));
        assertThrows(APIException.class, () -> Packer.parseCase("100 :: (1,53.38,€45)"));
        assertThrows(APIException.class, () -> Packer.parseCase("100 : ((1,53.38,€45))"));
        assertThrows(APIException.class, () -> Packer.parseCase("100 : (1,33.38,$45)"));
    }
}
