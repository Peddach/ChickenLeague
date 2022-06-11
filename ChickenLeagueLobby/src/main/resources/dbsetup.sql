CREATE TABLE IF NOT EXISTS ${mysql.version}_Arenas (
	ArenaName VARCHAR(50) PRIMARY KEY,
	ArenaState VARCHAR(50) NOT NULL,
	Type VARCHAR(50) NOT NULL,
	Players INT NOT NULL,
	Server VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS ${mysql.version}_Teleport (
	PlayerName VARCHAR(50) PRIMARY KEY,
	ArenaName VARCHAR(50) NOT NULL,
	Server VARCHAR(50) NOT NULL
);