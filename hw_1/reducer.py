#! /usr/bin/python
"""This is a Reduce-step"""

import sys


def read_mapper():
    """Read key-value pairs into input"""
    mapper_result = sys.stdin.read().splitlines()
    mapper_result = [job.split('\t') for job in mapper_result]
    return mapper_result

def get_answer():
    mapper_result = read_mapper()
    c = [int(job[0]) for job in mapper_result]
    m = [int(job[1]) for job in mapper_result]
    v = [int(job[2]) for job in mapper_result]
    if len(c)>1:
        while len(c)>1:
            cj, ck = c.pop(), c.pop()
            mj, mk = m.pop(), m.pop()
            vj, vk = v.pop(), v.pop()
            ci = cj + ck
            mi = (cj * mj + ck * mk) / ci
            vi = (cj * vj + ck * vk) / ci + cj * ck * ((mj - mk)/(cj + ck))**2
            c.append(ci)
            v.append(vi)
            m.append(mi)
    c = c[0]
    m = m[0]
    v = v[0]
    print(int(m), int(v))


if __name__ == '__main__':
    get_answer()
