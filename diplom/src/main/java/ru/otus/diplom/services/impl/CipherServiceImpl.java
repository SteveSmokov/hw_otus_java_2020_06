package ru.otus.diplom.services.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.otus.diplom.services.CipherService;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
public class CipherServiceImpl implements CipherService {
  private final String DECRYPT_KEY = "dGVzdF9rZXk=";//зашифрованный ключ в формате base64
  private Cipher cipher;

  @Override
  @SneakyThrows
  public String decrypt(@NonNull String data) {
    if (!data.isEmpty() && cipher != null) {
      byte[] dec = Base64.getDecoder().decode(data.getBytes());
      byte[] utf8 = cipher.doFinal(dec);
      return new String(utf8, StandardCharsets.UTF_8);
    } else return "";
  }

  @Override
  @SneakyThrows
  @PostConstruct
  public void createCipher() {
    byte[] decodedKey = Base64.getDecoder().decode(DECRYPT_KEY);
    SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
    cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.DECRYPT_MODE, key);

  }
}

