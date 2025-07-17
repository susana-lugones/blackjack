# Blackjack

This is a text-based card game simulation where the player competes against a virtual dealer to reach 21 without busting. It models core game logic, player-dealer interaction, and scoring, and is written in **Java** with an object-oriented design.

---

## **Overview**

This project is a command-line implementation of the popular casino card game **Blackjack**, developed in **Java**. It simulates key gameplay mechanics, including card dealing, hit/stand decisions, Ace handling (1 or 11), and win/loss conditions.

---

## **Key Features**

- **Card Deck Simulation**: Fully randomized, shuffled standard 52-card deck.  
- **Player & Dealer Logic**: Turn-based gameplay with AI-driven dealer decisions.  
- **Ace Handling**: Dynamic Ace value (1 or 11) depending on current hand value.  
- **Natural Blackjack Check**: Detects Blackjack on the initial deal.  
- **Scoring & Win Conditions**: Handles all possible outcomes: win, loss, tie, bust, and Blackjack.  
- **Command-Line Interaction**: Simple user prompts for hit/stand decisions.  
- **Replay Capability**: Option to play multiple rounds in a single session.  

---

## **Architecture**

The project is structured into the following core components:

### 1. **Card**

- Represents a single card with a `Suit` and `Rank`.  
- Includes utility methods to display card names and determine their values.

### 2. **Deck**

- Contains a full 52-card deck.  
- Supports shuffling and dealing cards.  
- Automatically resets when depleted.

### 3. **Hand**

- Stores a list of cards.  
- Calculates hand value, including logic for treating Aces as 1 or 11.  
- Handles bust and Blackjack detection.

### 4. **Player**

- Represents either the human player or the dealer.  
- Manages hand state and decision logic.

### 5. **Game Logic (`BlackjackGame`)**

- Handles turn-based logic between player and dealer.  
- Implements game loop, input prompts, scoring evaluation, and end-of-round summaries.  
- Detects special win conditions (e.g., dealer bust, player Blackjack).

### 6. **Utilities / Input Handling**

- Reads user input for decisions like "Hit" or "Stand".  
- Validates input and controls the flow of the game round-by-round.

---

## **How to Run**

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/blackjack.git
   cd blackjack
   ```
2. Compile the Java file:
   ```bash
   javac *.java
   ```
3. Run the game:
   ```bash
   java BlackjackGame
