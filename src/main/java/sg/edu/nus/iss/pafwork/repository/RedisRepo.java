package sg.edu.nus.iss.pafwork.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class RedisRepo {
  @Autowired
  private RedisTemplate<String, Object> template;

  public void add(String id, JsonObject j){
    template.opsForValue().set(id, j.toString());
    System.out.println("was this executed???");
  }
}
