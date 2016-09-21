mkdir ASV;
cd ASV;
svn2git https://svn.mpi.nl/LAT --authors ../users.txt --notags --branches metadata-browser/branches --trunk metadata-browser/trunk;
git remote add origin https://github.com/TheLanguageArchive/ASV.git;
git push --all -u;
git push --tags
cd ..;