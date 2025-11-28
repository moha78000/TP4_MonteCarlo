import pandas as pd
import matplotlib.pyplot as plt

# Charger les résultats
df = pd.read_csv("results.csv")

# Temps séquentiel : cas n_workers = 1
t1 = df[df["n_workers"] == 1]["temps_ms"].values[0]

# Calcul du speedup
df["speedup"] = t1 / df["temps_ms"]

plt.figure(figsize=(8,5))
plt.plot(df["n_workers"], df["speedup"], marker="o", label="Speedup mesuré")

# Courbe du speedup idéal (y = x)
plt.plot(df["n_workers"], df["n_workers"], linestyle='--', color='red', label="Speedup idéal")

plt.xlabel("Nombre de workers")
plt.ylabel("Speedup")
plt.title("Strong scaling Monte Carlo Pi")
plt.xticks(df["n_workers"])  # Pour que tous les workers soient visibles
plt.legend()
plt.grid(True)
plt.show()
