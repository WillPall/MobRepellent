default:
	cd src/mobrepellent; javac -cp ~mcuser/craftbukkit/craftbukkit.jar *java;
	cd src; jar -cf ../MobRepellent.jar mobrepellent/*.class plugin.yml;
	
