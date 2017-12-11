import sys

import matplotlib.pyplot as plt
import numpy as np

if len(sys.argv) != 3:
    print("Usage: python {0} <original_labels> <kmeans_labels>".format(sys.argv[0]))
    sys.exit(1)

original = sys.argv[1]
kmeans = sys.argv[2]

x_orig = []
y_orig = []
y_true = []

with open(original, 'r') as orig:
    for line in orig:
        x, y, label = [x.strip() for x in line.split(',')]
        x_orig.append(float(x))
        y_orig.append(float(y))
        y_true.append(label)

x_kmeans = []
y_kmeans = []
y_pred = []

with open(kmeans, 'r') as data:
    for line in data:
        x, y, label = [x.strip() for x in line.split(',')]
        x_kmeans.append(float(x))
        y_kmeans.append(float(y))
        y_pred.append(label)

rainbow_colors = plt.get_cmap('gist_rainbow')
colors = rainbow_colors(np.linspace(0, 1, 15))

print('Plotting original labels...')
color_labels = [colors[int(p) - 1] for p in y_true]

fig, ax = plt.subplots(figsize=(25, 15))
for i in range(len(x_orig)):
    ax.scatter(x_orig[i], y_orig[i], c=color_labels[i], s=20, alpha=0.8, marker="o")

ax.grid(True)
fig.savefig('plots/original.png')
plt.close(fig)

print('Plotting K-Means labels...')
color_labels = [colors[int(p)] for p in y_pred]

fig, ax = plt.subplots(figsize=(25, 15))
for i in range(len(x_kmeans)):
    ax.scatter(x_kmeans[i], y_kmeans[i], c=color_labels[i], s=20, alpha=0.8, marker="o")

ax.grid(True)
fig.savefig('plots/kmeans.png')
plt.close(fig)

print('Done')
