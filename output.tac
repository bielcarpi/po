global:
	a = 3

main:
	t9 = 4 * 5
	t9 = 3 + t9
	a = t9
	b = b + 1
	t9 = 8 * 5
	c = t9
	c = c - 1

hola:
	t9 = 6 * 2
	a = t9
E0:
	if a >= 7 goto E1
	a = 4
	goto E4
E1:
	if a >= 7 goto E2
	a = 5
	goto E4
E2:
	if a >= 7 goto E3
	c = c + 1
	t9 = 8 * 8
	t9 = 5 and t9
	b = t9
	goto E4
E3:
	a = 6
E4:
	t9 = 3 * 4
	b = t9
	z = z + 1


