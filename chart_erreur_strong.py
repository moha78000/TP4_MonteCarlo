import pandas as pd
import matplotlib.pyplot as plt

# Lire le CSV
data = pd.read_csv("erreurs_mw_strong.csv")

# Calculer le Speedup et l'Efficacité
T1 = data.loc[data['n_workers'] == 1, 'temps_ms'].values[0]  # temps avec 1 processus
data['Speedup'] = T1 / data['temps_ms']
data['Efficiency'] = data['Speedup'] / data['n_workers']

# Calculer les moyennes par nombre de travailleurs
mean_values = data.groupby('n_workers').agg({
    'Speedup': 'mean',
    'Efficiency': 'mean',
    'temps_ms': 'mean'
}).reset_index()

print("Moyenne par taille du nombre de travailleurs:")
print(mean_values)

# Tracer le Speedup (strong scaling)
plt.figure(figsize=(8, 5))

# Courbe du speedup mesuré
plt.plot(
    data['n_workers'],
    data['Speedup'],
    marker='o',
    color='blue',
    label='Speedup réel'
)

# Speedup idéal linéaire
plt.plot(
    [1, data['n_workers'].max()],
    [1, data['n_workers'].max()],
    linestyle='--',
    color='red',
    label='Speedup idéal'
)

# Afficher les valeurs au-dessus des points
for n, sp in zip(data['n_workers'], data['Speedup']):
    plt.text(n, sp + 0.1, f"{sp:.2f}", ha='center', va='bottom')

plt.title("Strong Scaling - Speedup vs Nombre de processus")
plt.xlabel("Nombre de processus")
plt.ylabel("Speedup")
plt.xticks(data['n_workers'])
plt.grid(True)
plt.legend()
plt.tight_layout()

# Afficher le graphique
plt.show()

# Sauvegarder le graphique avec une résolution de 300 DPI pour meilleure qualité
plt.savefig("strong_scaling.png", dpi=300)
