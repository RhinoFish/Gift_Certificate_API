package com.epam.esm.giftcertificate.services;

import com.epam.esm.giftcertificate.data.GiftRepository;
import com.epam.esm.giftcertificate.data.TagRepository;
import com.epam.esm.giftcertificate.model.Gift;
import com.epam.esm.giftcertificate.model.Tag;
import net.bytebuddy.asm.Advice;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GiftServiceUnitTest {

    @Mock
    private GiftRepository giftRepository;

    @InjectMocks
    private GiftService giftService;

    @Test
    public void getGiftsTest(){
        Map<String, List<Object>> cases = new HashMap<>();
        cases.put("Case1", Arrays.asList(new String[]{"name", "", "", "true"}));
        cases.put("Case2", Arrays.asList(new String[]{"createdate", "Vacation", "", "false"}));
        cases.put("Case3", Arrays.asList(new String[]{"lastupdatedate", "", "wine", "true"}));
        cases.put("Case4", Arrays.asList(new String[]{"name", "Friendship", "wo", "false"}));

        when(giftRepository.findAll()).thenReturn();

    }

    private void createMockGifts(){
        List<Gift> mockedGifts = new ArrayList<>();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        for(int i = 0; i<8; i++){
            Gift gift = new Gift();
            Long addHours = Long.valueOf((1000*60*60*(i+1)));
            gift.setId(Long.valueOf(i+1));
            if(i%2 ==0){
                gift.setName("Gift wine: " + i);
                gift.setDescription("This is a description");
                gift.setTags(Arrays.asList(new Tag[]{new Tag(1l,"Friendship"),new Tag(2l,"Vacation")}));
            }else{
                gift.setName("Gift: " + i);
                gift.setDescription("This is a description wo");
            }
            gift.setCreateDate(new Timestamp(time.getTime()+addHours));
            gift.setLastUpdateDate(new Timestamp(time.getTime()+addHours));





        }
    }
}
