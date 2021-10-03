#! /usr/bin/python3
"""This is a MAP-step"""

import sys
import csv


def read_input():
    lines = csv.reader(sys.stdin.read().splitlines())
    return lines


def main():
    """Create key-value pairs and print to stdout"""
    array_lines = read_input()
    prices_str = [line[9] for line in array_lines if line[9] != ''][1:]
    prices = [float(price) for price in prices_str]
    ck = len(prices)
    mk = sum(prices)/ck
    vk = (sum([(price - mk)**2 for price in prices])/ck)
    print('%d\t%d\t%d' % (ck, mk, vk))
    print('%d\t%d\t%d' % (ck, mk, vk))

if __name__ == '__main__':
    main()
