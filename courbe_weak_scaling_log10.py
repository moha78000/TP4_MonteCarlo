import pandas as pd
import matplotlib.pyplot as plt

# 1. Charger le CSV
file_name = "erreurs_mw_weak.csv"
df = pd.read_csv(file_name)

# 2. Calcul de N par worker (vérification weak scaling)
df["n_par_worker"] = df["ntotal"] / df["n_workers"]

# 3. Agrégation par nombre de workers (médiane)
df_med = (
    df.groupby("n_workers", as_index=False)
      .median(numeric_only=True)
)

# 4. Tracé : temps médian en fonction du nombre de workers
plt.figure()
plt.plot(
    df_med["n_workers"],
    df_med["temps_ms"],
    marker="o",
    linestyle="-"
)

plt.xlabel("Nombre de workers")
plt.ylabel("Temps d'exécution médian (ms)")
plt.title("Weak scaling – Temps d'exécution (médiane par nombre de workers)")
plt.grid(True, linestyle="--", alpha=0.5)

plt.show()
