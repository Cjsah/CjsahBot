package net.cjsah.bot.http;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.BotEvent;
import net.cjsah.bot.event.type.TencentEventType;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.Utils;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

public final class HttpServerOperate {

    static JSONObject verifyAccount(ExchangeResolver resolver) {
        JSONObject result = resolver.data().getJSONObject("d");
        String token = result.getString("plain_token");
        String ts = result.getString("event_ts");

        try {
            StringBuilder seedBuilder = new StringBuilder(resolver.secret());
            while (seedBuilder.length() < 32) {
                seedBuilder.append(resolver.secret());
            }
            String seed = seedBuilder.substring(0, 32);
            EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
            Signature signature = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
            EdDSAPrivateKeySpec privateKeySpec = new EdDSAPrivateKeySpec(seed.getBytes(), spec);
            PrivateKey privateKey = new EdDSAPrivateKey(privateKeySpec);
            signature.initSign(privateKey);
            signature.update((ts + token).getBytes());
            String sig = Utils.bytesToHex(signature.sign());
            result.put("signature", sig);
            return result;
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            BotHttpServerImpl.log.error("Signature Failed!", e);
            return null;
        }
    }

    static JSONObject processServerMsg(ExchangeResolver resolver) {
        BotEvent event = TencentEventType.createEvent(resolver.data());
        EventManager.broadcast(event);
        return null;
    }

}
