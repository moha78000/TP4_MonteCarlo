import pandas as pd
import matplotlib.pyplot as plt

# 1️⃣ Lire le CSV
data = pd.read_csv("pi_results.csv")

# 2️⃣ Calculer le Speedup et l'Efficacité
T1 = data.loc[data['nprocess'] == 1, 'temps_ms'].values[0]  # temps avec 1 thread
data['Speedup'] = T1 / data['temps_ms']
data['Efficiency'] = data['Speedup'] / data['nprocess']

print(data[['nprocess', 'temps_ms', 'Speedup', 'Efficiency']])

# 3️⃣ Tracer le graphique
plt.figure(figsize=(8,5))
plt.plot(data['nprocess'], data['Speedup'], marker='o', color='blue', label='Speedup réel')
plt.plot([1, max(data['nprocess'])], [1, max(data['nprocess'])], 
         linestyle='--', color='red', label='Speedup idéal (linéaire)')

# Ajouter les valeurs de speedup au-dessus des points
for i, sp in enumerate(data['Speedup']):
    plt.text(data['nprocess'][i], sp + 0.1, f"{sp:.2f}", ha='center')

plt.title("Strong Scaling - Speedup vs Nombre de threads")
plt.xlabel("Nombre de threads")
plt.ylabel("Speedup")
plt.xticks(data['nprocess'])
plt.legend()
plt.grid(True)
plt.tight_layout()

# 4️⃣ Afficher le graphique
plt.show()

# 5️⃣ (Optionnel) Sauvegarder le graphique en PNG
plt.savefig("strong_scaling.png")
