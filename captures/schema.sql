Table sections {
  sectionId int [pk]
  sectionName varchar
  shapeIndex int
  x double
  y double
  rotation double
  scale double
}

Table sieges {
  siegeId int [pk]
  numSiege int
  sectionId int
  radius double
  scale double
  x double
  y double
}

Table concerts {
  concertId int [pk]
  concertName varchar
  concertDate date
  concertTime varchar
  concertDescription text
  posterImg varchar
  artists varchar
}

Table matches {
  matchId int [pk]
  eventName varchar
  eventDate date
  eventTime varchar
  team1 varchar
  team2 varchar
  team1Logo varchar
  team2Logo varchar
  description varchar
}

Table reservations {
  ResId int [pk]
  ResEventID int
  ResUserID int
  ResSectionID int
  ResSiegeID int
  Price double
  created_at timestamp
}

Table resale {
  ResaleID int [pk]
  eventID int
  sectionID int
  siegeID int
  newPrice double
  created_at timestamp
}

Table users {
  UserID int [pk]
  username varchar
  password varchar
  isAdmin tinyint
  email varchar
  Tel varchar
  created_at timestamp
}

Table switch {
  SwitchID int [pk]
  eventID int
  sectionID int
  siegeID int
  created_at timestamp
}

Table tickets {
  TicketID int [pk]
  TicEventID int
  TicPrice int
  TicSiegeID int
  TicDate date
  TicTime varchar
  created_at timestamp
}

Table concert_section_prices {
  concertId int
  sectionId int
  price float
  created_at timestamp
}

Table match_section_prices {
  matchId int
  sectionId int
  price float
  created_at timestamp
}

Ref: sections.sectionId > sieges.sectionId
Ref: sections.sectionId > reservations.ResSectionID
Ref: sieges.siegeId > reservations.ResSiegeID
Ref: sieges.siegeId > resale.siegeID
Ref: sieges.siegeId > switch.siegeID
Ref: sieges.siegeId > tickets.TicSiegeID
Ref: users.UserID > reservations.ResUserID
Ref: concerts.concertId > concert_section_prices.concertId
Ref: sections.sectionId > concert_section_prices.sectionId
Ref: matches.matchId > match_section_prices.matchId
Ref: sections.sectionId > match_section_prices.sectionId
