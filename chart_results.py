import pandas as pd
import matplotlib.pyplot as plt

# 1️⃣ Lire le CSV
data = pd.read_csv("results.csv")  

# 2️⃣ Calculer le Speedup et l'Efficacité
# Pour strong scaling, on compare au temps avec 1 thread
T1 = data.loc[data['n_workers'] == 1, 'temps_ns'].values[0]

data['Speedup'] = T1 / data['temps_ns']
data['Efficiency'] = data['Speedup'] / data['n_workers']

print(data[['n_workers', 'temps_ns', 'Speedup', 'Efficiency']])

# 3️⃣ Tracer le graphe du Speedup
plt.figure(figsize=(8,5))
plt.plot(data['n_workers'], data['Speedup'], marker='o', color='blue', label='Speedup réel')
plt.plot([1, max(data['n_workers'])], [1, max(data['n_workers'])], 
         linestyle='--', color='red', label='Speedup idéal (linéaire)')

# Ajouter les valeurs de Speedup au-dessus des points
for i, sp in enumerate(data['Speedup']):
    plt.text(data['n_workers'][i], sp + 0.05, f"{sp:.2f}", ha='center')

plt.title("Strong Scaling - Speedup vs Nombre de threads")
plt.xlabel("Nombre de threads")
plt.ylabel("Speedup")
plt.xticks(data['n_workers'])
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()

# 4️⃣ Tracer l'efficacité
plt.figure(figsize=(8,5))
plt.plot(data['n_workers'], data['Efficiency'], marker='o', color='green', label='Efficacité')
plt.axhline(1, linestyle='--', color='red', label='Efficacité idéale')
plt.title("Strong Scaling - Efficacité vs Nombre de threads")
plt.xlabel("Nombre de threads")
plt.ylabel("Efficacité")
plt.xticks(data['n_workers'])
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()
