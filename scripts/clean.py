import os
import shutil


def clean():
    print("Starting cleanup")

    for (directory, subdirectories, files) in os.walk(os.path.join(os.getcwd(), "..")):
        if str(directory).endswith("build") or str(directory).endswith("target") or str(directory).endswith("out"):
            shutil.rmtree(directory)
            print("Removed directory:", directory)
            for file in files:
                print("    -> ", file)

    pass


if __name__ == '__main__':
    clean()