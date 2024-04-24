package com.katerynamykh.taskprofitsoft.backend.dto.chain;

import com.katerynamykh.taskprofitsoft.backend.model.RestorantChain;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChainWithLocationsDto {
    private RestorantChain mainChainInfo;
    private List<String> locationAddress;
}
