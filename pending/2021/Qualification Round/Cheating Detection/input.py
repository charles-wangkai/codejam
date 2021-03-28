import math
import random
import sys

T = 100
print(T)

P = 86
print(P)


def generate(si, qj):
    return '1' if random.random() <= 1 / (1 + math.exp(qj - si)) else '0'


def is_cheat():
    return random.random() <= 0.5


for tc in range(T):
    cheater = random.randint(0, 99)
    print('Case #{}: {}'.format(tc + 1, cheater + 1), file=sys.stderr)

    s = [random.uniform(-3, 3) for _ in range(100)]
    q = [random.uniform(-3, 3) for _ in range(10000)]

    for i in range(100):
        if i == cheater:
            print(''.join('1' if is_cheat() else generate(
                s[i], q[j]) for j in range(10000)))
        else:
            print(''.join(generate(s[i], q[j]) for j in range(10000)))
