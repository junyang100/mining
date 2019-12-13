package com.mine.controller;

import com.alibaba.fastjson.JSON;
import com.mine.crypto.bip32.ExtendedKey;
import com.mine.crypto.bip39.MnemonicGenerator;
import com.mine.crypto.bip39.RandomSeed;
import com.mine.crypto.bip39.SeedCalculator;
import com.mine.crypto.bip39.WordCount;
import com.mine.crypto.bip39.wordlists.English;
import com.mine.util.FileUtils;
import com.mine.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    @RequestMapping(value = "/init", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String init(){
        //产生随机数 WordCount 可以选择需要生成的助记词长度
        byte[] random = RandomSeed.random(WordCount.TWELVE);

        //产生助记词 记得传入需要语言
        List<String> mnemonic = new MnemonicGenerator(English.INSTANCE).createMnemonic(random);
        System.out.println(JSON.toJSONString(mnemonic));

        //产生 seed
        byte[] seed = new SeedCalculator().calculateSeed(mnemonic, "");
        System.out.println(StringUtils.byteArrayToHexStr(seed));

        //可以通过 seed 产生公私钥
        try {
            ExtendedKey extendedKey = ExtendedKey.create(seed);
            String key = extendedKey.serialize(false);
            FileUtils.writeToFile("/Users/gaoyang/key.txt",key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

}
