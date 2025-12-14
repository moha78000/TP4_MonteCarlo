import math
import pandas as pd
import matplotlib.pyplot as plt

# 1. Charger le CSV
file_name = "erreurs_mw_strong.csv"
df = pd.read_csv(file_name)  # colonnes: temps_ms, pi_valeur, erreur_avant, error_percent, ntotal, n_workers

# 2. Calcul de log10(erreur) si absent
if "log10_error" not in df.columns:
    df["abs_error"] = df["erreur_avant"].abs()
    df = df[df["abs_error"] > 0]  # éviter log10(0)
    df["log10_error"] = df["abs_error"].apply(math.log10)

# 3. Agrégation par Ntotal et n_workers (médiane)
df_med = df.groupby(["ntotal", "n_workers"], as_index=False).median(numeric_only=True)

# 4. Tracé : couleur et légende selon nombre de workers
plt.figure()
for n_worker in sorted(df_med["n_workers"].unique()):
    subset = df_med[df_med["n_workers"] == n_worker]
    plt.scatter(
        subset["ntotal"],
        subset["log10_error"],
        label=f"{n_worker} workers"
    )

plt.xscale("log")  # axe X en log si nécessaire
plt.xlabel("Ntotal (nombre total de points)")
plt.ylabel("log10(erreur absolue) – médiane")
plt.title("Erreur de l’approximation de π en fonction de Ntotal")
plt.grid(True, which="both", linestyle="--", alpha=0.4)
plt.legend(title="Nombre de workers", bbox_to_anchor=(1.05, 1), loc='upper left')
plt.tight_layout()
plt.show()
