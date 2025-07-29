import pandas as pd
import matplotlib.pyplot as plt

# Provide column names explicitly since the CSV has no header row
column_names = ['Crossover rate', 'Mutation Rate', 'Fitness']
df = pd.read_csv("/Users/jayshah/Developer/Brock University/COSC 3P71/GA/averagesInverseMutationTwoPointCrossover2.csv", header=None, names=column_names)

# Add a 'Generations' column (you have 10 points per (crossover, mutation))
df['Generations'] = list(range(10, 110, 10)) * (len(df) // 10)

# Group by (Crossover Rate, Mutation Rate)
grouped = df.groupby(['Crossover rate', 'Mutation Rate'])

# Plot
plt.figure(figsize=(12, 6))
for (crossover, mutation), group in grouped:
    label = f"Crossover={crossover}, Mutation={mutation}"
    sorted_group = group.sort_values('Generations')
    plt.plot(sorted_group['Generations'], sorted_group['Fitness'], label=label)

plt.xlabel('Generations')
plt.ylabel('Fitness')
plt.title('Two-Point Crossover and Inverse Mutation for Data2.txt')
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()
