package module.webservice.services.example2;

import java.util.*;

/**
 * Created by root on 2015/12/30.
 */
public class TeamsUtility {
    private Map<String, Team> team_map;

    public TeamsUtility() {
        team_map = new HashMap<>();
    }

    public Team getTeam(String name) {
        return team_map.get(name);
    }

    public List<Team> getTeams() {
        List<Team> list = new ArrayList<>();
        Set<String> keys = team_map.keySet();
        for (String key : keys)
            list.add(team_map.get(key));
        return list;
    }


    public void make_test_teams() {
        List<Team> teams = new ArrayList<Team>();
        Player chico = new Player("Leonard Marx", "Chico");
        Player groucho = new Player("Julius Marx", "Groucho");
        Player harpo = new Player("Adolph Marx", "Harpo");
        List<Player> mb = new ArrayList<>();
        mb.add(chico);
        mb.add(groucho);
        mb.add(harpo);
        Team marx_brothers = new Team("Marx Brothers", mb);
        teams.add(marx_brothers);
        store_teams(teams);
    }

    private void store_teams(List<Team> teams) {
        for (Team team : teams)
            team_map.put(team.getName(), team);
    }
}
