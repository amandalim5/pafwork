package sg.edu.nus.iss.pafwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.JsonObject;
import sg.edu.nus.iss.pafwork.repository.RedisRepo;

@Service
public class LogAuditService {

  @Autowired
  RedisRepo redisRepo;

  public void add(String s, JsonObject j){
    redisRepo.add(s, j);

  }
}
