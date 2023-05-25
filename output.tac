PRINT_INT:
	syscall 1
	a0 = 0xA
	syscall 11
	ret 0

sortVars:
	i = 0
	j = 0
	aux = 0
	i = 0
E0:
	if i >= n goto E1
E2:
	if j >= 4 goto E4
E3:
E5:
	if j != 0 goto E7
E6:
E8:
	if v1 <= v2 goto E10
E9:
	aux = v1
	v1 = v2
	v2 = aux
E10:
	goto E13
E7:
	if j != 1 goto E12
E11:
E12:
	if j != 2 goto E17
E13:
	if v2 <= v3 goto E15
E14:
	aux = v2
	v2 = v3
	v3 = aux
E15:
	goto E13
E16:
E17:
	if j != 3 goto E22
E18:
	if v3 <= v4 goto E20
E19:
	aux = v3
	v3 = v4
	v4 = aux
E20:
	goto E13
E21:
E22:
	j = j + 1
	goto E3
E23:
	if v4 <= v5 goto E25
E24:
	aux = v4
	v4 = v5
	v5 = aux
E25:
E4:
	i = i + 1
	goto E0
E1:
	ret 0

global:
	v1 = 5
	v2 = 2
	v3 = 15
	v4 = 1
	v5 = 1000

main:
	n = 5
	savec
	addp 0 n
	call sortVars
	loadc
	savec
	addp 0 v1
	call print
	loadc
	savec
	addp 0 v2
	call print
	loadc
	savec
	addp 0 v3
	call print
	loadc
	savec
	addp 0 v4
	call print
	loadc
	savec
	addp 0 v5
	call print
	loadc
	ret 0


