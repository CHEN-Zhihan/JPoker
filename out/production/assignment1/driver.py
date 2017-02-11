import subprocess


for i in range(1, 110):
    subprocess.Popen("java -Djava.security.policy=security.policy ClientUI localhost login {} {} &".format(i, i), shell=True, stdout=subprocess.PIPE)