import pandas as pd
import matplotlib.pyplot as plt

# 1. Lire le CSV (on utilise le fichier Weak Scaling)
data = pd.read_csv("erreurs_mw_weak.csv")

# 2. Calculer la médiane par nombre de workers
# Pour le weak scaling, on s'intéresse surtout à la stabilité du temps_ms
median_values = data.groupby('n_workers').agg({
    'temps_ms': 'median'
}).reset_index()

# 3. Temps de référence T(1) = médiane avec 1 worker
T1 = median_values.loc[median_values['n_workers'] == 1, 'temps_ms'].values[0]

# 4. Calcul de l'Efficacité Weak Scaling : E = T(1) / T(n)
# Idéalement, T(n) est proche de T(1), donc E est proche de 1
median_values['Efficiency'] = T1 / median_values['temps_ms']

print("Résultats Weak Scaling (Médianes) :")
print(median_values)

# --- Graphique 1 : Temps d'exécution (doit être le plus plat possible) ---
plt.figure(figsize=(10, 5))
plt.subplot(1, 2, 1)
plt.plot(median_values['n_workers'], median_values['temps_ms'], marker='s', color='red', label='Temps mesuré')
plt.axhline(y=T1, color='gray', linestyle='--', label='Temps idéal (constant)')
plt.title("Temps d'exécution (Weak Scaling)")
plt.xlabel("Nombre de processus")
plt.ylabel("Temps (ms)")
plt.grid(True)
plt.legend()

# --- Graphique 2 : Efficacité (doit être proche de 1) ---
plt.subplot(1, 2, 2)
plt.plot(median_values['n_workers'], median_values['Efficiency'], marker='o', color='green', label='Efficacité mesurée')
plt.axhline(y=1.0, color='blue', linestyle='--', label='Efficacité idéale (100%)')

# Affichage des valeurs d'efficacité sur les points
for n, eff in zip(median_values['n_workers'], median_values['Efficiency']):
    plt.text(n, eff + 0.02, f"{eff*100:.1f}%", ha='center')

plt.title("Efficacité du Weak Scaling")
plt.xlabel("Nombre de processus")
plt.ylabel("Efficacité (T1 / Tn)")
plt.ylim(0, 1.2) # Pour mieux voir la chute
plt.grid(True)
plt.legend()

plt.tight_layout()
plt.savefig("weak_scaling_analysis.png", dpi=300)
plt.show()