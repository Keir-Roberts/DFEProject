package com.example.project.enums;

public enum SkillPoints {
POINTSMAX(10), ABILITYCOST(3);

private final int points; 
SkillPoints(int points) {
	this.points = points;
}
public int getPoints() {
	return points;
}
}
