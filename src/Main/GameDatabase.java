package Main;

import Entity.Entity;
import Entity.NPC_Samurai;
import Entity.Player;
import Object.SuperObject;
import Object.OBJ_Crystals;
import Object.OBJ_Key;
import Entity.NPC_FlyingEye;
import Entity.NPC_Goblin;
import Entity.NPC_Sardonix;
import Entity.NPC_Warrior;

import java.sql.*;

public class GameDatabase {
    private static final String DB_URL = "jdbc:sqlite:src/res/database/gameData.db";
    private static Connection conn;

    // Aici conectam database
    public static void connect() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            Class.forName("org.sqlite.JDBC");
            System.out.println("Connected to the database.");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
    }

    // Inchidem conexiunea
    public static void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.out.println("Disconnection failed: " + e.getMessage());
        }
    }

    public static void createTablesIfNotExist() {
        try (Statement stmt = conn.createStatement()) {
            String sqlPlayer = """
                CREATE TABLE IF NOT EXISTS player_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                worldX INTEGER NOT NULL,
                worldY INTEGER NOT NULL,
                lives INTEGER NOT NULL,
                hp INTEGER,
                level INTEGER,
                score INTEGER
            );
            """;

            String sqlNpc = """
            CREATE TABLE IF NOT EXISTS world_npc (
                id INTEGER PRIMARY KEY,
                type TEXT NOT NULL,
                worldX INTEGER NOT NULL,
                worldY INTEGER NOT NULL,
                level INTEGER NOT NULL,
                isAlive BOOLEAN NOT NULL,
                maxHp INTEGER,
                hp INTEGER
            );
            """;

            String sqlInventory = """
            CREATE TABLE IF NOT EXISTS inventory (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                blackCrystal INTEGER NOT NULL,
                pinkCrystal INTEGER NOT NULL,
                redCrystal INTEGER NOT NULL,
                blueCrystal INTEGER NOT NULL,
                purpleCrystal INTEGER NOT NULL,
                weapon INTEGER NOT NULL
            );
            """;
            String sqlCrystals = """
            CREATE TABLE IF NOT EXISTS world_objects (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                worldX INTEGER NOT NULL,
                worldY INTEGER NOT NULL,
                collected BOOLEAN NOT NULL,
                level INTEGER NOT NULL,
                UNIQUE(name, worldX, worldY, level)
            );
            """;
            stmt.execute(sqlPlayer);
            stmt.execute(sqlNpc);
            stmt.execute(sqlInventory);
            stmt.execute(sqlCrystals);
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int getHighestScore() {
        int highestScore = 0;

        String sql = "SELECT MAX(score) AS max_score FROM player_data";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    highestScore = rs.getInt("max_score");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while fetching highest score: " + e.getMessage());
            e.printStackTrace();
        }

        return highestScore;
    }
    public int saveNewPlayer(Player player, int level) {
        String sql = "INSERT INTO player_data (worldX, worldY, lives, hp, level, score) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, player.worldX);
            pstmt.setInt(2, player.worldY);
            pstmt.setInt(3, player.lives);
            pstmt.setInt(4, player.hp);
            pstmt.setInt(5, level);
            pstmt.setInt(6, player.score);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // error
    }


    public void saveInventory(GamePanel gp) {
        String sql = "INSERT INTO inventory (blackCrystal, pinkCrystal, redCrystal, blueCrystal, purpleCrystal, weapon) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, gp.crystalsBlack);
            pstmt.setInt(2, gp.crystalsPink);
            pstmt.setInt(3, gp.crystalsRed);
            pstmt.setInt(4, gp.crystalsBlue);
            pstmt.setInt(5, gp.crystalsPurple);
            pstmt.setInt(6, gp.selectedWeapon);

            pstmt.executeUpdate();
            System.out.println("Inventory saved!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveWorldNPCs(Entity[] npcs) {

        String sql = "INSERT INTO world_npc (id, type, worldX, worldY, level, isAlive, maxHp, hp) VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(id) DO UPDATE SET worldX=excluded.worldX, worldY=excluded.worldY, isAlive=excluded.isAlive, hp=excluded.hp";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Entity npc : npcs) {
                if (npc != null) {
                    pstmt.setInt(1, npc.id);
                    pstmt.setString(2, npc.type);
                    pstmt.setInt(3, npc.worldX);
                    pstmt.setInt(4, npc.worldY);
                    pstmt.setInt(5, npc.level);
                    pstmt.setBoolean(6, npc.alive);
                    pstmt.setInt(7, npc.maxHp);
                    pstmt.setInt(8, npc.hp);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveWorldObjects(SuperObject[] objects) {
        String sql = "INSERT INTO world_objects (name, worldX, worldY, collected, level) VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT(name, worldX, worldY, level) DO UPDATE SET collected=excluded.collected";
        int i = -1;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (SuperObject superObject : objects) {

                i++;
                if (superObject == null) {
                    System.out.println("Null object at index: " + i);
                    continue;
                }
                if (superObject != null) {
                    pstmt.setString(1, superObject.name);
                    pstmt.setInt(2, superObject.worldX);
                    pstmt.setInt(3, superObject.worldY);
                    pstmt.setBoolean(4, superObject.collected);
                    pstmt.setInt(5, superObject.level);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isWorldObjectsTableEmpty() {
        String sql = "SELECT COUNT(*) AS count FROM world_objects";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isNPCsTableEmpty() {
        String sql = "SELECT COUNT(*) AS count FROM world_npc";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void loadInventory(GamePanel gp) {
        String sql = "SELECT * FROM inventory ORDER BY id DESC LIMIT 1";
        int selectedWeapon = 0;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                gp.crystalsBlack = rs.getInt("blackCrystal");
                gp.crystalsPink = rs.getInt("pinkCrystal");
                gp.crystalsRed = rs.getInt("redCrystal");
                gp.crystalsBlue = rs.getInt("blueCrystal");
                gp.crystalsPurple = rs.getInt("purpleCrystal");
                gp.selectedWeapon = rs.getInt("weapon");

                System.out.println("Inventory loaded!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int loadPlayerData(Player player) {
        String sql = "SELECT * FROM player_data ORDER BY id DESC LIMIT 1"; // Se incarca cel mai recent "save"

        int level = 0;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                player.worldX = rs.getInt("worldX");
                player.worldY = rs.getInt("worldY");
                player.lives = rs.getInt("lives");
                player.hp = rs.getInt("hp");
                player.score = rs.getInt("score");

                level = rs.getInt("level");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return level;
    }

    public void loadAllWorldObjects(SuperObject[] objects) {
        String sql = "SELECT * FROM world_objects";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            int index = 0;

            while (rs.next() && index < objects.length) {
                String name = rs.getString("name");
                int worldX = rs.getInt("worldX");
                int worldY = rs.getInt("worldY");
                boolean collected = rs.getBoolean("collected");
                int level = rs.getInt("level");

                SuperObject obj = null;

                if (name.startsWith("Crystal_")) {
                    String[] parts = name.split("_");
                    if (parts.length >= 2) {
                        String color = parts[1];
                        obj = new OBJ_Crystals(color);
                    }
                } else if (name.equalsIgnoreCase("Map") || name.equalsIgnoreCase("OBJ_Key")) {
                    obj = new OBJ_Key("Map");
                }

                if (obj != null) {
                    obj.level = level;
                    obj.worldX = worldX;
                    obj.worldY = worldY;
                    obj.collected = collected;
                    objects[index++] = obj;
                } else {
                    System.out.println("Unknown object type in DB: " + name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadAllWorldNPCs(GamePanel gp, Entity[] npcs) {
        String sql = "SELECT * FROM world_npc";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            int index = 0;

            while (rs.next() && index < npcs.length) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                int worldX = rs.getInt("worldX");
                int worldY = rs.getInt("worldY");
                boolean isAlive = rs.getBoolean("isAlive");
                int hp = rs.getInt("hp");
                int level = rs.getInt("level");
                int maxHp = rs.getInt("maxHp");

                Entity npc = null;

                if ("Samurai".equalsIgnoreCase(type)) {
                    npc = new NPC_Samurai(gp);
                } else if ("FlyingEye".equalsIgnoreCase(type)) {
                    npc = new NPC_FlyingEye(gp);
                } else if ("Goblin".equalsIgnoreCase(type)) {
                    npc = new NPC_Goblin(gp);
                } else if ("Sardonix".equalsIgnoreCase(type)) {
                    npc = new NPC_Sardonix(gp);
                } else if ("Warrior".equalsIgnoreCase(type)) {
                    npc = new NPC_Warrior(gp);
                }

                if (npc != null) {
                    npc.id = id;
                    npc.type = type;
                    npc.worldX = worldX;
                    npc.worldY = worldY;
                    npc.alive = isAlive;
                    npc.hp = hp;
                    npc.level = level;
                    npc.maxHp = maxHp;

                    if (id >= 0 && id < npcs.length) {
                        npcs[id] = npc;
                    } else {
                        System.out.println("WARNING: ID " + id + " out of bounds for npc array.");
                    }
                } else {
                    System.out.println("Unknown NPC type in DB: " + type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadWorldNPCs(GamePanel gp, Entity[] npcs, int currentLevel) {
        String sql = "SELECT * FROM world_npc WHERE level = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentLevel);
            ResultSet rs = pstmt.executeQuery();

            int index = 0;
            while (rs.next() && index < npcs.length) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                int worldX = rs.getInt("worldX");
                int worldY = rs.getInt("worldY");
                boolean isAlive = rs.getBoolean("isAlive");
                int hp = rs.getInt("hp");
                int level = rs.getInt("level");
                int maxHp = rs.getInt("maxHp");

                Entity npc = null;

                // Cream NPC
                if ("Samurai".equalsIgnoreCase(type)) {
                    npc = new NPC_Samurai(gp);
                } else if ("FlyingEye".equalsIgnoreCase(type)) {
                    npc = new NPC_FlyingEye(gp);
                } else if ("Goblin".equalsIgnoreCase(type)) {
                    npc = new NPC_Goblin(gp);
                } else if ("Sardonix".equalsIgnoreCase(type)) {
                    npc = new NPC_Sardonix(gp);
                } else if ("Warrior".equalsIgnoreCase(type)) {
                    npc = new NPC_Warrior(gp);
                }

                if (npc != null) {
                    npc.id = id;
                    npc.type = type;
                    npc.worldX = worldX;
                    npc.worldY = worldY;
                    npc.alive = isAlive;
                    npc.hp = hp;
                    npc.level = level;
                    npc.maxHp = maxHp;

                    if (id >= 0 && id < npcs.length) {
                        npcs[id] = npc;
                    } else {
                        System.out.println("WARNING: ID " + id + " out of bounds for npc array.");
                    }
                } else {
                    System.out.println("Unknown NPC type in DB: " + type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadWorldObjects(SuperObject[] objects, int currentLevel) {
        String sql = "SELECT * FROM world_objects WHERE level = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentLevel); // nivel curent
            ResultSet rs = pstmt.executeQuery();

            int index = 0;
            while (rs.next() && index < objects.length) {
                String name = rs.getString("name");
                int worldX = rs.getInt("worldX");
                int worldY = rs.getInt("worldY");
                boolean collected = rs.getBoolean("collected");
                int level = rs.getInt("level");

                SuperObject obj = null;

                if (name.startsWith("Crystal_")) {
                    String[] parts = name.split("_");
                    if (parts.length >= 2) {
                        String color = parts[1];
                        obj = new OBJ_Crystals(color);
                    }
                } else if (name.equalsIgnoreCase("Map") || name.equalsIgnoreCase("OBJ_Key")) {
                    obj = new OBJ_Key("Map");
                }


                if (obj != null) {
                    obj.level = level;
                    obj.worldX = worldX;
                    obj.worldY = worldY;
                    obj.collected = collected;
                    objects[index++] = obj;
                } else {
                    System.out.println("Unknown object type in DB: " + name);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}