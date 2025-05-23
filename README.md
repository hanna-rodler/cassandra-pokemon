# Cassandra-pokemon
For the course "Big Data"

Dataset: https://www.kaggle.com/datasets/rounakbanik/pokemon/ <- add to `src/main/resources`.

Exercise 1 (Create a table for Pokemon) Create a table holding the entire Pokemon information, and insert the entire CSV data.

Exercise 2 (Query Pokemon Types) Query the distribution of Pokemon types (Type
1 and Type 2 combined) across all generations. Show the count of Pokemon for each type combination.

Exercise 3 (Query Stats by Generation) Query the average HP, Attack, Defense,
and Speed for each generation, but only for non-legendary Pokemon. Sort the results by generation.

Exercise 4 (Query Legendary Pokemon) Query all legendary Pokemon and their
stats, grouped by their primary type (Type 1). For each type, show the count of legendary
Pokemon and their average total stats.

Exercise 5 (Use Case: Pokemon Training Center) Model additional tables to satisfy the following use cases:
* As a trainer, I want to track my Pokemon’s training progress, including their current level,
experience points, and training sessions completed.
* As a trainer, I want to be able to have multiple Pokemon of the same species, each with
their own unique training history.
* As a trainer, I want to see which Pokemon types I have trained the most and their average
improvement in stats.

Exercise 6 (Use Case: Pokemon Training Center) Create a batch statement that:
* Records a new training session for a Pokemon
* Updates the Pokemon’s experience points and level
* Updates the trainer’s statistics for that Pokemon type
Ensure that all updates are atomic and consistent.