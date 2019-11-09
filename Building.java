package buildingdatabase;

public class Building {

	OneBuilding data;
	Building older;
	Building same;
	Building younger;

	public Building(OneBuilding data){
		this.data = data;
		this.older = null;
		this.same = null;
		this.younger = null;
	}

	public String toString(){
		String result = this.data.toString() + "\n";
		if (this.older != null){
			result += "older than " + this.data.toString() + " :\n";
			result += this.older.toString();
		}
		if (this.same != null){
			result += "same age as " + this.data.toString() + " :\n";
			result += this.same.toString();
		}
		if (this.younger != null){
			result += "younger than " + this.data.toString() + " :\n";
			result += this.younger.toString();
		}
		return result;
	}

	public Building addBuilding (OneBuilding b){
		if (b==null) { //empty argument
			throw new NullPointerException("Invalid OneBuilding entry!");
		}
		if (b.equals(this.data)) { //one sole node
			return this;
		}
		if (this.data==null) { //empty tree
			this.data=b;
			//DO I NEED TO RETURN THIS EVERYWHERE
		}
		if (b.yearOfConstruction<this.data.yearOfConstruction) { //toAdd is older
			if (this.older==null) {
				this.older = new Building(b);
			}
			else {
				this.older.addBuilding(b);
			}
		}
		if (b.yearOfConstruction>this.data.yearOfConstruction) { //toAdd is younger
			if (this.younger==null) {
				this.younger = new Building(b);
			}
			else {
				this.younger.addBuilding(b);
			}
		}
		if (b.yearOfConstruction==this.data.yearOfConstruction) { //toAdd is same age
			Building temp = new Building(this.data);
			if (b.height<=this.data.height) { //toAdd is shorter
				if (this.same==null) {
					this.same = new Building(b);
				}
				else {
					this.same.addBuilding(b);
				}
			}
			else if (b.height>this.data.height) { //toAdd is taller
				this.data = b;
				if (this.same==null) {
					this.same = new Building(temp.data);
				}
				else {
					this.same.addBuilding(temp.data);
				}
			}
		}
		return this; // DON'T FORGET TO MODIFY THE RETURN IF NEEDS BE
	}

	public Building addBuildings (Building b){
		this.addBuilding(b.data); 
		if(b.older != null) { //base1
			this.addBuildings(b.older);
		}
		if(b.same != null) { //base2
			this.addBuildings(b.same);
		}
		if(b.younger != null) { //base3
			this.addBuildings(b.younger);
		}
		return this; // DON'T FORGET TO MODIFY THE RETURN IF NEEDS BE
	}

	public Building removeBuilding (OneBuilding b){
		if (b==null) { //empty argument
			throw new NullPointerException("Invalid OneBuilding entry!");
			//return this;
		}
		if (this.data==null) { //if empty
			return this;
		}
		else {
			if (this.older!=null && this.older.data.equals(b)) { //leaf
				if (this.older.younger==null && this.older.same==null && this.older.older==null) {
					this.older.data=null;
					this.older=null;
					return this;
				}
			}
			if (this.same!=null && this.same.data.equals(b)) { //leaf
				if (this.same.younger==null && this.same.same==null && this.same.older==null) {
					this.same.data=null;
					this.same=null;
					return this;
				}
			}
			if (this.younger!=null && this.younger.data.equals(b)) { //leaf
				if (this.younger.younger==null && this.younger.same==null && this.younger.older==null) {
					this.younger.data=null;
					this.younger=null;
					return this;
				}
			}
			if (this.data.equals(b)) { //not leaf
				if (this.same!=null) { //we can just shift same up
					this.data = this.same.data;
					this.same = this.same.same;
					return this;
				}
				else if (this.older!=null) { //shift older up and reorganize
					Building y = this.younger; //store younger info before replacing
					this.data = this.older.data;
					this.same = this.older.same;
					this.younger = this.older.younger;
					this.older = this.older.older;
					if (y!=null) { //if we must re-add younger
						if (this.younger!=null) {
							this.younger = this.younger.addBuildings(y); //readding younger
						}
						else if (this.younger == null) {
							this.younger = y; 
						}
					}
					if (this!=null) {
						return this; //never return null
					}
				}
				else if (this.younger!=null) { //shift younger up and reorganize
					this.data = this.younger.data;
					this.older = this.younger.older;
					this.same = this.younger.same;
					this.younger = this.younger.younger;
					if (this!=null) {
						return this; //never return null
					}
				}
			}
			else {  //check next level (recursion)
				if (this.older!=null) {
					this.older.removeBuilding(b);
				}
				if (this.same!=null) {
					this.same.removeBuilding(b);
				}
				if (this.younger!=null) {
					this.younger.removeBuilding(b);
				}
			}
		}
		return this; //return new tree
	}

	public int oldest(){
		Building oldest = this;
		while (oldest.older!=null) { //walk down this.older
			oldest = oldest.older;
		}
		return oldest.data.yearOfConstruction; // DON'T FORGET TO MODIFY THE RETURN IF NEEDS BE
	}

	public int highest(){
		int tall = this.data.height; //temp to store tallest
		if (this.older!=null && this.older.data.height>=tall) { //if older is taller, replace tall
			tall = this.older.data.height;
			tall = this.older.highest();
		}
		if (this.younger!=null && this.younger.data.height>=tall) { //if younger is taller, replace tall
			tall = this.younger.data.height;
			tall = this.younger.highest();
		}
		return tall; // DON'T FORGET TO MODIFY THE RETURN IF NEEDS BE
	}

	public OneBuilding highestFromYear (int year){
		OneBuilding tall = null; //temp to store tallest
		if (this.data.yearOfConstruction==year) { //starting with a year match (tallest will be at top)
			tall = this.data;
		}
		else if (this.older!=null && this.data.yearOfConstruction>year) {
			tall = this.older.highestFromYear(year); //check older if year is too low
		}
		if (this.younger!=null && this.data.yearOfConstruction<year) {
			tall = this.younger.highestFromYear(year); //check younger if year is too high
		}
		return tall;
	}

	public int numberFromYears (int yearMin, int yearMax){
		int counter = 0; //count starts at 0
		if (yearMin>yearMax) { //confirming start year is smaller than end year
			return 0;
		}
		else {
			Building test = this;
			if (test.data.yearOfConstruction<=yearMax && test.data.yearOfConstruction>=yearMin) {
				counter++; //add if from years
			}
			if (test.older!=null) {
				counter+= test.older.numberFromYears(yearMin, yearMax); //check children
			}
			if (test.same!=null) {
				counter+= test.same.numberFromYears(yearMin, yearMax);
			}
			if (test.younger!=null) {
				counter+= test.younger.numberFromYears(yearMin, yearMax);
			}
		}
		return counter; // DON'T FORGET TO MODIFY THE RETURN IF NEEDS BE
	}

	public int[] costPlanning (int nbYears){
		int[] a = new int[nbYears]; //initialize empty array
		Building test = this; //test building
		for (int i = 0; i<nbYears; i++) {
			if (test!=null && test.data.yearForRepair==2018+i) { //add age
				a[i]+=test.data.costForRepair; 
			}
			if (test.older!=null) { //add ages for all children with recursion
				a[i]+= test.older.costPlanning(nbYears)[i];
			}
			if (test.same!=null) {
				a[i]+= test.same.costPlanning(nbYears)[i];
			}
			if (test.younger!=null) {
				a[i]+= test.younger.costPlanning(nbYears)[i];
			}
		}
		return a; // DON'T FORGET TO MODIFY THE RETURN IF NEEDS BE
	}
}
