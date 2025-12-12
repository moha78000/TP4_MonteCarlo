import math
import pandas as pd
import matplotlib.pyplot as plt

# 1. Charger le CSV
file_name = "erreurs_mw_strong.csv"
df = pd.read_csv(file_name)  # colonnes: temps_ms,pi_valeur,erreur_avant,error_percent,ntotal,n_workers ou log10_error si tu l'as ajouté

# 2. Si tu n'as PAS encore log10_error dans le CSV, on le calcule ici
if "log10_error" not in df.columns:
    # erreur absolue
    df["abs_error"] = df["erreur_avant"].abs()
    # filtrer les lignes où l'erreur est nulle (log10 impossible)
    df = df[df["abs_error"] > 0]
    df["log10_error"] = df["abs_error"].apply(math.log10)

# 3. Tracer un nuage de points (points seuls)
plt.figure()
plt.scatter(df["ntotal"], df["log10_error"], marker="o", color="blue")

plt.xscale("log")  # optionnel: axe X en log si ntotal varie beaucoup
plt.xlabel("Ntotal (nombre total de points)")
plt.ylabel("log10(erreur absolue)")
plt.title("Erreur de l’approximation de π en fonction de Ntotal")
plt.grid(True, which="both", linestyle="--", alpha=0.4)

plt.show()