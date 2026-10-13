import csv

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
    reader = csv.reader([line])
    row = next(reader)
    row[1] = row[1].replace("'", "''")
    row[1] = "'" + row[1] + "'"
    line = ','.join(row)
    

    print(f'INSERT INTO publikacje (ID,Tytuł,Rok,Autorzy,Punkty) VALUES ({line});')

print('')
print('SELECT * FROM publikacje WHERE Rok = 2020 AND Punkty >= Autorzy * 100 ORDER BY Autorzy;')

print('DROP TABLE publikacje;')