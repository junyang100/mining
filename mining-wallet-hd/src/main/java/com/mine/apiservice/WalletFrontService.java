package com.mine.apiservice;

import com.alibaba.fastjson.JSON;
import com.mine.crypto.CoinTypes;
import com.mine.crypto.ECKeyPair;
import com.mine.crypto.Transaction;
import com.mine.crypto.bip32.ExtendedKey;
import com.mine.crypto.bip44.AddressIndex;
import com.mine.crypto.bip44.BIP44;
import com.mine.crypto.bip44.CoinPairDerive;
import com.mine.crypto.bitcoin.BTCTransaction;
import com.mine.crypto.bitcoin.BitCoinECKeyPair;
import com.mine.crypto.utils.HexUtils;
import com.mine.hdService.dto.*;
import com.mine.util.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mine.crypto.Key;

@Controller
@RequestMapping("/front/wallet")
public class WalletFrontService {

    @RequestMapping(value = "/address", method = RequestMethod.POST)
    @ResponseBody
    HdResult<AddressResp> getAddress(AddressReq req){
        try {
            String key = FileUtils.readFromFile("D:\\key.txt");
            ExtendedKey extendedKey = ExtendedKey.parse(key);
            Key master3 = extendedKey.getMaster();
            BitCoinECKeyPair parse2 = BitCoinECKeyPair.parse(master3, true);
            System.out.println(parse2.getPrivateKey());
            System.out.println(parse2.getPublicKey());
            System.out.println(parse2.getAddress());

            AddressIndex btc = BIP44.m().purpose44()
                    .coinType(CoinTypes.BitcoinTest)
                    .account(0)
                    .external()
                    .address(1);
            CoinPairDerive coinKeyPair = new CoinPairDerive(extendedKey);
            ECKeyPair pairBtc = coinKeyPair.derive(btc);

            AddressIndex eth = BIP44.m().purpose44()
                    .coinType(CoinTypes.Ethereum)
                    .account(0)
                    .external()
                    .address(0);
            ECKeyPair pairEth = coinKeyPair.derive(eth);
            AddressResp resp = AddressResp.builder().btc(pairBtc.getAddress()).eth(pairEth.getAddress()).build();
            return HdResult.<AddressResp>builder().build().success(resp);
        } catch (Exception e) {
            e.printStackTrace();
            return HdResult.<AddressResp>builder().build().fail();

        }
    }

    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    @ResponseBody
    HdResult<SignResp> sign(SignReq req){
        try {
            //1. 通过 tx 转换
            Transaction transaction= new BTCTransaction(HexUtils.fromHex("01000000013814c518646cc8040cfc5e0e03022b0bdaf705bacde10ff924da473e67d569df0000000000ffffffff0280969800000000001976a914073126b0fd55e5551257f6e17e7a8082a5b1ca3988ac00093d00000000001976a9147023b06ab19d25ede77fe77c2de67510f6b9b68388ac00000000"));// HexUtils.fromHex(str) 可以转换
            System.out.println(JSON.toJSONString(transaction));
            //签名
            String key = FileUtils.readFromFile("D:\\key.txt");
            ExtendedKey extendedKey = ExtendedKey.parse(key);
            Key master1 = extendedKey.getMaster();
            BitCoinECKeyPair parse = BitCoinECKeyPair.parse(master1, true);
            byte[] signByte = transaction.sign(parse);
            String signTx = HexUtils.toHex(signByte);
            System.out.println(signTx);
        }catch (Exception e){
            System.out.println(e);
        }
        return HdResult.<AddressResp>builder().build().success(null);
    }

}
