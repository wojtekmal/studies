print('''CREATE TABLE publikacje (
    ID int PRIMARY KEY,
    Tytuł varchar(200) NOT NULL,
    Rok int NOT NULL,
    Autorzy int NOT NULL,
    Punkty int NOT NULL
);
''')

f = open('Publikacje.csv', 'r')
f.readline()

for i in range(1, 390):
    line = f.readline()
    groups = line.split('"')

    if len(groups) == 1:
        vals = line.split(',')
    else:
        print(groups)
        vals = groups[0].split(',') + [groups[1]] + groups[2].split(',')
    
    vals[1] = '\'' + vals[1] + '\''

    line = ','.join(vals)
    line = line[:-1]

    print(f'INSERT INTO publikacje (ID,Tytuł,Rok,Autorzy,Punkty) VALUES ({line});')

print('')
print('SELECT * FROM publikacje WHERE Rok = 2020 AND Punkty >= Autorzy * 100 ORDER BY Autorzy;')

print('DROP TABLE publikacje;')