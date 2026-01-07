import pandas as pd
import matplotlib.pyplot as plt

# Lire le CSV
data = pd.read_csv("erreurs_mw_strong.csv")

# Calculer la médiane par nombre de workers
median_values = data.groupby('n_workers').agg({
    'temps_ms': 'median'
}).reset_index()

# Temps de référence T(1) = médiane avec 1 worker
T1 = median_values.loc[median_values['n_workers'] == 1, 'temps_ms'].values[0]

# Calcul du Speedup et de l'Efficacité à partir des médianes
median_values['Speedup'] = T1 / median_values['temps_ms']
median_values['Efficiency'] = median_values['Speedup'] / median_values['n_workers']

print("Médiane par nombre de workers :")
print(median_values)

# Tracer le Speedup (strong scaling)
plt.figure(figsize=(8, 5))

# Courbe du speedup mesuré (médiane)
plt.plot(
    median_values['n_workers'],
    median_values['Speedup'],
    marker='o',
    label='Speedup médian'
)

# Speedup idéal linéaire
plt.plot(
    median_values['n_workers'],
    median_values['n_workers'],
    linestyle='--',
    label='Speedup idéal'
)


plt.title("Strong Scaling – Speedup médian vs Nombre de processus")
plt.xlabel("Nombre de processus")
plt.ylabel("Speedup")
plt.grid(True)
plt.legend()
plt.tight_layout()

# Sauvegarder puis afficher
plt.savefig("strong_scaling_median.png", dpi=300)
plt.show()
