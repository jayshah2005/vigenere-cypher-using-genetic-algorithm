from scipy.stats import ttest_ind_from_stats

# Sample size
n = 30

# Method names and (mean, std) tuples
d1 = {
    "Uniform + Inverse": (0.084779096, 0.001302799),
    "Uniform + ASCII":   (0.084091798, 0.001854411),
    "Two-point + Inverse": (0.193205691, 0.056040256),
    "Two-point + ASCII":  (0.195460684, 0.046056048)
}

# Method names and their (mean, std dev) from Data2.txt
d2 = {
    "Uniform + Inverse": (0.455891048, 0.003334905),
    "Uniform + ASCII":   (0.454456655, 0.003508236),
    "Two-point + Inverse": (0.492969971, 0.075183883),
    "Two-point + ASCII":  (0.520833660, 0.036196025)
}

Combined_d1 = {
    "Uniform": (
        0.084779096 + 0.084091798,  # Mean
        (0.001302799**2 + 0.001854411**2)**0.5  # Std dev
    ),
    "Two-point": (
        0.193205691 + 0.195460684,
        (0.056040256**2 + 0.046056048**2)**0.5
    )
}

Combined_d2 = {
    "Uniform": (
        0.455891048 + 0.454456655,
        (0.003334905**2 + 0.003508236**2)**0.5
    ),
    "Two-point": (
        0.492969971 + 0.520833660,
        (0.075183883**2 + 0.036196025**2)**0.5
    )
}

names = list(methods.keys())

print(f"{'':<30}", end='')
for name in names:
    print(f"{name:<23}", end='')
print()

for name1 in names:
    print(f"{name1:<30}", end='')
    for name2 in names:
        if name1 == name2:
            print(f"{'-':<23}", end='')
        else:
            mean1, std1 = methods[name1]
            mean2, std2 = methods[name2]
            t_stat, p_val = ttest_ind_from_stats(mean1, std1, n, mean2, std2, n, equal_var=False)
            print(f"{p_val:.6f}               ", end='')
    print()