package com.example.piedpiperdb.DAO.JavaFXActions;

import com.example.piedpiperdb.DAO.GameDAO;
import com.example.piedpiperdb.DAO.MatchDAO;
import com.example.piedpiperdb.DAO.PlayerDAO;
import com.example.piedpiperdb.DAO.TeamDAO;
import com.example.piedpiperdb.Entities.Game;
import com.example.piedpiperdb.Entities.Player;
import com.example.piedpiperdb.Entities.Team;
import com.example.piedpiperdb.View.AlertBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

//GEFP-27-SJ
public class TeamActions {

    private static final TeamDAO TEAM_DAO = new TeamDAO();
    private static final PlayerDAO PLAYER_DAO = new PlayerDAO();
    private static final GameDAO GAME_DAO = new GameDAO();
    private static final MatchDAO MATCH_DAO = new MatchDAO();

//TableView
//----------------------------------------------------------------------------------------------------------------------

    public static VBox getTableViewAllTeams(VBox vBox) {
        List<Team> teams = TEAM_DAO.getAllTeams();
        return showTable(vBox, teams);
    }

    public static VBox getTableViewSelectedTeams (VBox vBox, List<String> selections){

        List<Integer> ids = new ArrayList<>();
        for (String selection : selections) {
            try {
                String[] parts = selection.split(" ");

                String lastPart = parts[parts.length - 1];
                int id = Integer.parseInt(lastPart);
                ids.add(id);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                System.out.println("Could not parse id: " + selection);
            }
            System.out.println("IDs to query: " + ids);
        }
        List<Team> teams = TEAM_DAO.getTeamsByGame(ids);
        //System.out.println(teams.size());
        return showTable(vBox, teams );
    }

    public static List<CheckBox> gameCheckBoxes (){
        List<Game> games = GAME_DAO.getAllGames();
        List<CheckBox> checkBoxes = new ArrayList<>();

        for (Game game : games) {
            String gameName = game.getGameName();
            int gameId = game.getGameId();

            CheckBox checkBox = new CheckBox(gameName + ", Game ID: " + gameId);
            checkBoxes.add(checkBox);
        }
        return checkBoxes;
    }

// Show Table & Create Team Table
//----------------------------------------------------------------------------------------------------------------------

    public static VBox showTable(VBox vBox, List<Team> teams){

        vBox.getStyleClass().add("textFieldOne");
        TableView<Team> table = createTeamTable(teams);
        table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(table, Priority.ALWAYS);
        vBox.getChildren().add(table);
        return vBox;

    }

    private static TableView<Team> createTeamTable(List<Team> teams){
        ObservableList<Team> teamsObservableList = FXCollections.observableArrayList(teams);

        TableView<Team> tableView = new TableView<>();

        TableColumn<Team, String> team_id = new TableColumn<>("Team ID");
        team_id.setCellValueFactory(new PropertyValueFactory<>("teamId"));


        TableColumn<Team, String> team_name = new TableColumn<>("Team Name");
        team_name.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<Team, String> game_name = new TableColumn<>("Game");
        game_name.setCellValueFactory(new PropertyValueFactory<>("gameName"));

        TableColumn<Team, String> player_nickname = new TableColumn<>("Players");
        player_nickname.setCellValueFactory(cellData ->{
            Team team = cellData.getValue();
            String Nicknames = team.getListOfPlayersInTeam().stream()
                    .map(Player::getNickname)
                    .collect(Collectors.joining("\n"));
            return new SimpleStringProperty(Nicknames);
        });

        tableView.getColumns().addAll(team_id, team_name, game_name, player_nickname);
        tableView.setItems(teamsObservableList);
        return tableView;
    }

// ListViews
//----------------------------------------------------------------------------------------------------------------------

 /*   public static ListView<String> createPlayerListView(List<Player> players){
        ListView<String> playerListView = (ListView<String>) players;

    }*/


// CRUDs (Team Actions for FX)
//----------------------------------------------------------------------------------------------------------------------

    //Create
    public static boolean createTeam(Team team) {
        return TEAM_DAO.createTeam(team);
    }
/*    // public static boolean createTeam(Team team){return TEAM_DAO.createTeam(team);} //SJ:  Lät denna vara kvar trotts No Usage - Känns rimligt

    public static boolean createTeamWithList(Team team) {
        if (TEAM_DAO.createTeam(team)) {
            // Update each player's teamId in the database
            for (Player player : team.getListOfPlayersInTeam()) {
                PLAYER_DAO.updatePlayer(player);
            }
            return true;
        }
        return false;
    }
    public static Team createTeamFromFieldsInput (String teamName, String selectedGameValue, String selectedPlayerValue){

        Team team = new Team(teamName);

        if (selectedGameValue != null && !selectedGameValue.isEmpty()) {
            int gameId = Integer.parseInt(selectedGameValue.split(",")[0].trim());
            Game selectedGame = GAME_DAO.getGameById(gameId);
            team.setGameId(selectedGame);
        }

        if (selectedPlayerValue != null && !selectedPlayerValue.isEmpty()) {

            //WIP
        }


        return team;
    }*/

    //Read
    public static Team getTeamById(int teamId){return TEAM_DAO.getTeamById(teamId);}
    public static List<Team> getTeamsByGame (int gameId){
        return TEAM_DAO.getTeamsByGame(List.of(gameId));
    }
    public static List<Team> getAllTeams(){return TEAM_DAO.getAllTeams();}
    public static List<Team> getAllOtherTeamsExcluding (String teamName) {
        List<Team> allTeams = TEAM_DAO.getAllTeams();
        //System.out.println(allTeams.size());

        List<Team> allOtherTeams = allTeams.stream()
                .filter(team -> !team.getTeamName().equalsIgnoreCase(teamName))
                .toList();

        return allOtherTeams;

    }

    //Update
    public static boolean updateTeam(Team team){ return TEAM_DAO.updateTeam(team);}

    //Delete
    public static void deleteTeam(Team team){TEAM_DAO.deleteTeam(team);} //SJ:  Lät denna vara kvar trotts No Usage - Känns rimligt
    public static boolean deleteTeamById(int teamId){return TEAM_DAO.deleteTeamById(teamId);}

    // Checkers - Booleans
    public static boolean isTeamNameUnique(String teamName){return TEAM_DAO.isTeamNameUnique(teamName);}
    public static boolean validateNewTeamName(String teamName){
        if (teamName == null || teamName.isEmpty()) {
            AlertBox.displayAlertBox("Error", "Team name is required.");
            return false;
        }
        if (TeamActions.isTeamNameUnique(teamName)) {
            AlertBox.displayAlertBox("Error", "Team name already exists. Choose another.");
            return false;
        }
        return true;
    }

// Get Actions
//----------------------------------------------------------------------------------------------------------------------

    public static Game getGameById(int gameId){return GAME_DAO.getGameById(gameId);}
    public static List<Game> getAllGames(){return GAME_DAO.getAllGames();}

    public static Player getPlayerById(int playerId){return PLAYER_DAO.getPlayer(playerId);}
    public static List<Player> getAllPlayers(){return PLAYER_DAO.getAllPlayers();}
    public static List<Player> getAllAvailablePlayers(){
        return PLAYER_DAO.getAllPlayers().stream()
                .filter(player -> player.getTeamId() == null) // Only players without a team
                .toList();
    }
    public static List<Player> getPlayersInTeam(Team team){
        return team.getListOfPlayersInTeam();
    }
    public static String getPlayerNicknames (List<Player> listOfPlayers ){
        String nicknames = "";

        for (Player player : listOfPlayers) {
            nicknames = nicknames + player.getNickname() + ", ";
        }

        return nicknames;
    }
    public static List<Player> getPlayersByGame(int gameId) {
        if (gameId == 0) {
            return Collections.emptyList();
        }
        return PLAYER_DAO.getAllPlayersFromSelectedGame(List.of(gameId));
    }

    public static void updatePlayerTeamId(Player player, Team team) {
        player.setTeamId(team);
        PLAYER_DAO.updatePlayer(player);
    }

    // WIPS
/*    public static ListView<String> createAvailablePlayersListView() {
        ListView<String> playerListView = new ListView<>();
        List<Player> availablePlayers = getAllAvailablePlayers(); // Fetch players with gameId == null

        ObservableList<String> playerNicknames = FXCollections.observableArrayList();
        for (Player player : availablePlayers) {
            playerNicknames.add(player.getId() + ", " + player.getNickname());
        }

        playerListView.setItems(playerNicknames);
        playerListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playerListView.getStyleClass().add("list-cell");
        // playerListView.setPrefWidth(100);

        return playerListView;
    }*/

    public static ListView<String> createPlayerListView(List<Player> players) {

        ListView<String> playerListView = new ListView<>();
        ObservableList<String> playerNicknames = FXCollections.observableArrayList();

        for (Player player : players) {
            playerNicknames.add(player.getId() + ", " + player.getNickname()); // Use the nickname and ID as the list item
        }

        playerListView.setItems(playerNicknames);

        playerListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playerListView.getStyleClass().add("list-cell");
        playerListView.setPrefWidth(100);

        return playerListView;
    }


}
