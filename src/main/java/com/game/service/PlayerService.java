package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerService {
        private final PlayerRepository playerRepository;

        public PlayerService(PlayerRepository playerRepository) {
                this.playerRepository = playerRepository;
        }

        public List<Player> players(Pageable pageable) {
                return playerRepository.findAll(pageable).toList();
        }

        public List<Player> allPlayers() {
                return playerRepository.findAll();
        }

        public List<Player> findByParams(Map<String, String> params) {
                String name = params.getOrDefault("name", null);
                String title = params.getOrDefault("title", null);
                Race race = params.containsKey("race") ? Race.valueOf(params.get("race")) : null;
                Profession profession = params.containsKey("profession") ? Profession.valueOf(params.get("profession")) : null;
                Date after = null;
                if (params.containsKey("after")) {
                        after = new Date(Long.parseLong(params.get("after")));
                }
                Date before = null;
                if (params.containsKey("before")) {
                        before = new Date(Long.parseLong(params.get("before")));
                }
                Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;
                Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : null;
                Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : null;
                Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
                Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;
                Pageable pageable;
                int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
                int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));
                if (params.containsKey("order")) {
                        pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, (PlayerOrder.valueOf(params.get("order"))).getFieldName());
                } else {
                        pageable = PageRequest.of(pageNumber, pageSize);
                }
                return playerRepository.findAllByParams(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, pageable).stream().collect(Collectors.toList());
        }

        public Integer count(Map<String, String> params) {
                String name = params.getOrDefault("name", null);
                String title = params.getOrDefault("title", null);
                Race race = params.containsKey("race") ? Race.valueOf(params.get("race")) : null;
                Profession profession = params.containsKey("profession") ? Profession.valueOf(params.get("profession")) : null;
                Date after = null;
                if (params.containsKey("after")) {
                        after = new Date(Long.parseLong(params.get("after")));
                }
                Date before = null;
                if (params.containsKey("before")) {
                        before = new Date(Long.parseLong(params.get("before")));
                }
                Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;
                Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : null;
                Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : null;
                Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
                Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;
                return playerRepository.findAllByParamsCount(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
        }

        public Player add(Map<String, String> params) {
                try {
                        Player player = new Player();
                        player.setName(params.get("name"));
                        player.setTitle(params.get("title"));
                        player.setRace(params.get("race"));
                        player.setProfession(params.get("profession"));
                        player.setBirthday(params.get("birthday"));
                        player.setBanned(params.get("banned"));
                        player.setExperience(params.get("experience"));
                        if (player.getName() != null & player.getTitle() != null & player.getRace() != null & player.getProfession() != null &
                                player.getBirthday() != null & player.getBanned() != null & player.getExperience() != null) {

                                return playerRepository.save(player);
                        }
                } catch (Exception e) {

                }
                return null;

        }

        public boolean existsById(Long id) {
                return playerRepository.existsById(id);
        }

        public Player findById(Long id) {
                return playerRepository.findById(id).get();
        }

        public Player updatePlayer(Long id, Map<String, String> params) {
                if (!playerRepository.findById(id).isPresent() || params == null) return null;
                Player player = playerRepository.findById(id).get();
                String name = params.getOrDefault("name", null);
                String title = params.getOrDefault("title", null);
                Race race = params.containsKey("race") ? Race.valueOf(params.get("race")) : null;
                Profession profession = params.containsKey("profession") ? Profession.valueOf(params.get("profession")) : null;
                String birthday = params.containsKey("birthday") ? params.get("birthday") : null;
                Boolean banned = params.containsKey("banned") ? "true".equals(params.get("banned")) : null;
                String experience = params.containsKey("experience") ? params.get("experience") : null;
                params.clear();
                if (name != null) player.setName(name);
                if (title != null) player.setTitle(title);
                if (race != null) player.setRace(race.toString());
                if (profession != null) player.setProfession(profession.toString());
                if (birthday != null) player.setBirthday(birthday);
                if (banned != null) player.setBanned(banned.toString());
                if (experience != null) player.setExperience(experience);
                return playerRepository.saveAndFlush(player);

        }

        public String deleteById(Long id) {
                boolean isShipExists = playerRepository.existsById(id);
                if (isShipExists) {
                        playerRepository.deleteById(id);
                        return "200";
                } else {
                        return "404";
                }
        }

        public boolean isParamsValid(Map<String, String> params) {
                String name = params.getOrDefault("name", null);
                String title = params.getOrDefault("title", null);
                Integer prodDate = null;
                if (params.containsKey("birthday")) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(params.get("birthday")));
                        prodDate = calendar.get(Calendar.YEAR);
                }


                Integer experience = params.containsKey("experience") ? Integer.parseInt(params.get("experience")) : null;
                boolean result =
                        (name == null || title == null || name.length() <= 12 && name.length() > 0 && title.length() <= 30 && title.length() > 0)
                                && (prodDate == null || prodDate >= 2000 && prodDate <= 3000)
                                && (experience == null || experience >= 0 && experience <= 10_000_000);
                try {
                        if (params.containsKey("race")) {
                                Race race = Race.valueOf(params.get("race"));

                        }
                        if (params.containsKey("profession")) {

                                Profession profession = Profession.valueOf(params.get("profession"));
                        }
                } catch (IllegalArgumentException | NullPointerException e) {
                        result = false;
                }
                return result;
        }
}
