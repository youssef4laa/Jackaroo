# Jackaroo: A New Game Spin

**Jackaroo: A New Game Spin** is a modern, single-player adaptation of the classic Middle Eastern board/card game Jackaroo. This project was developed for the Computer Programming Lab, Spring 2025, at the German University in Cairo. Players face off against three intelligent CPU opponents on a dynamic 100-cell track, leveraging strategy, luck, and custom mechanics to be the first to bring all their marbles home.

## Table of Contents

* [Introduction](#introduction)
* [Features](#features)
* [Game Overview](#game-overview)
* [Game Rules](#game-rules)
* [Card Mechanics](#card-mechanics)
* [Setup & Installation](#setup--installation)
* [Usage](#usage)
* [Project Structure](#project-structure)
* [Contributing](#contributing)
* [License](#license)

## Introduction

Jackaroo is a beloved strategic board/card game with deep roots in Middle Eastern culture. Our version reimagines Jackaroo for solo play against three CPU players, adding fresh challenges with new cards, randomized trap cells, and tailored rules for a dynamic experience.

### Our Unique Spin

* **Single-Player Focus**: Face off against three CPU opponents, each with unique personalities.
* **Custom Cards & Rules**: 15 distinct card types, including two wild cards, introduce novel tactical options.
* **Dynamic Board**: Eight trap cells randomly reposition every time one is triggered.

## Features

* **Strategic Depth**: Balances skill, planning, and chance.
* **AI Opponents**: Three CPU players make randomized but rule-compliant moves.
* **Rich Mechanics**: Movement, swapping, burning, saving, and skipping powered by specialized cards.
* **Data-Driven**: Card definitions and frequencies stored in `data/Cards.csv`.

## Game Overview

* **Players**: 1 human vs. 3 CPU
* **Board**: 100-cell main track + individual Home, Safe, and Base zones
* **Deck**: 102 cards (standard ranks Ace–King + 2 wild cards)
* **Fire Pit**: Discard pile that refills the deck when empty
* **Marbles**: Four per player, each color-coded and tracked by zone

## Game Rules

### Marble Movement

* Move according to a card’s rank or effect.
* Exact counts required; collisions destroy opponent marbles.
* Trap cells reset marble to Home Zone and relocate on trigger.

### Marble Swapping (Jack)

* Swap one of your marbles with an opponent’s marble on the track.
* Cannot swap if either marble is in a Base or Safe cell.

### Marble Fielding (Ace & King)

* Introduce new marbles from Home Zone to Base cell.
* Base cell occupancy and Home Zone count enforced.

### Marble Burning & Saving (Wild Cards)

* **Burner Wild Card**: Destroy a target opponent marble on track.
* **Saver Wild Card**: Rescue one of your marbles to a random Safe cell.

### Discarding & Skipping (Ten & Queen)

* Remove an opponent’s card and skip their turn if they have cards in hand.

For full details on card actions, see [docs/CARD\_TABLE.md](docs/CARD_TABLE.md).

## Card Mechanics

All card types, frequencies, and descriptions are listed in `data/Cards.csv`. A visual table is available in the `docs/` folder for quick reference.

## Setup & Installation

### Prerequisites

* Python 3.8+
* pip package manager

### Installation Steps

```bash
# Clone the repository
git clone https://github.com/yourusername/jackaroo-spin.git
cd jackaroo-spin

# Install dependencies
pip install -r requirements.txt
```

## Usage

To start the game:

```bash
python main.py
```

Follow on-screen prompts to enter your name and play through rounds until someone wins.

## Project Structure

```
Jackaroo/
├── src/
│   ├── controller/
│   ├── engine/
│   │   ├── Game.java
│   │   ├── GameLogic.java
│   │   ├── GameManager.java
│   │   └── package-info.java
│   ├── engine/board/
│   ├── exception/
│   ├── factory/
│   ├── images/cards/
│   ├── model/
│   │   ├── card/
│   │   │   ├── Card.java
│   │   │   ├── Deck.java
│   │   │   ├── Marble.java
│   │   │   ├── package-info.java
│   │   │   ├── standard/
│   │   │   │   ├── Ace.java
│   │   │   │   ├── Two.java
│   │   │   │   ├── Three.java
│   │   │   │   ├── Four.java
│   │   │   │   ├── Five.java
│   │   │   │   ├── Six.java
│   │   │   │   ├── Seven.java
│   │   │   │   ├── Eight.java
│   │   │   │   ├── Nine.java
│   │   │   │   ├── Ten.java
│   │   │   │   ├── Jack.java
│   │   │   │   ├── Queen.java
│   │   │   │   ├── King.java
│   │   │   │   ├── Standard.java
│   │   │   │   └── Suit.java
│   │   │   └── wild/
│   │   └── player/
│   └── view/
├── resources/
│   ├── audio/
│   └── images/
├── test/
└── README.md
```

## Contributing

Contributions, issues, and feature requests are welcome! Please:

1. Fork the repository
2. Create a new branch (`git checkout -b feature-name`)
3. Commit your changes (`git commit -m 'Add feature'`)
4. Push to the branch (`git push origin feature-name`)
5. Open a pull request

## License

This project is distributed under the MIT License. See [LICENSE](LICENSE) for details.
