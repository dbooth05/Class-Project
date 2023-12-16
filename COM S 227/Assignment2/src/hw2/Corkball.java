package hw2;

/**
 * Model of an obscure game called "Corkball" or sometimes "Fuzzball", generally
 * played in the St Louis area.  It is vaguely similar to baseball, except that
 * it is much simpler since there are no actual bases and the runners are imaginary.
 * See the assignment pdf for further explanation.
 * 
 * @author Dylan Booth
 */
public class Corkball
{
  /**
   * Number of strikes causing a player to be out.
   */
  public static final int MAX_STRIKES = 2;

  /**
   * Number of balls causing a player to walk.
   */
  public static final int MAX_BALLS = 5;

  /**
   * Number of outs before the teams switch.
   */
  public static final int MAX_OUTS = 3;

  //TODO: EVERYTHING ELSE
  
  //instance variables
  private int inning; 
  private boolean isTopOfInning;
  private int ballCount;
  private int strikeCount;
  private int outs;
  private int maxInnings;
  private int scoreZero;
  private int scoreOne;
  private int i;
  private boolean bases[] = new boolean[3];
  
  public Corkball(int givenNumInnings) {
	  
	  inning = 1;
	  isTopOfInning = true;	
	  ballCount = 0;
	  strikeCount = 0;
	  outs = 0;
	  maxInnings = givenNumInnings;
	  for(int i = 0; i < 3; i++) {
		  bases[i] = false;
	  }
  }
  
  public void ball() {
	  
	  //indicates bad pitch where batter did not swing
	  ballCount++;
	  if(ballCount == MAX_BALLS) {
		  countReset();
		  shiftRunnersForWalk();
	  }
  }
  
  public void caughtFly() {
	  
	  //indicates an out by a ball being caught
	  countReset();
	  if(outs < MAX_OUTS) {
		  outs++;
	  }
	  if(outs == MAX_OUTS) {
		  switchTeam();
		  gameEnded();
	  }
  }
  
  public boolean gameEnded() {
	  if(inning > maxInnings) {
		  countReset();
		  outs = 0;
		  for(i = 0; i < 3; i++) {
			  bases[i] = false;
		  }
		  return true;
	  }
	  else {
		  return false;
	  }  
  }
  
  public int getBallCount() {
	  
	  if(gameEnded()) {
		  ballCount = 0;
	  }
	  return ballCount;
  }
  
  public int getCalledStrikes() {
	  
	  if(gameEnded()) {
		  strikeCount = 0;
	  }
	  return strikeCount;
  }
  
  public int getCurrentOuts() {
	  
	  if(outs == 0 && gameEnded()) {
		  outs = 0;
	  }
	  return outs;
  }
  
  public int getTeam0Score() {
	  
	  if(gameEnded()) {
		  scoreZero = 0;
	  }
	  return scoreZero;
  }
  
  public int getTeam1Score() {
	  
	  if(gameEnded()) {
		  scoreOne = 0;
	  }
	  return scoreOne;
  }
  
  public void hit(int distance) {
	  
	  //determines foul, hit with distance
	  if(distance < 15) {
		  countReset();
		  outs++;
		  if(outs == MAX_OUTS) {
			  switchTeam();
			  gameEnded();
		  }
	  }
	  //single
	  else if(distance >= 15 && distance < 150) {
		  shiftRunners(1);
		  ballCount = 0;
		  strikeCount = 0;
	  }
	  //double
	  else if(distance >= 150 && distance < 200) {
		  shiftRunners(2);
		  ballCount = 0;
		  strikeCount = 0;
	  }
	  //triple
	  else if(distance >= 200 && distance < 250) {
		  shiftRunners(3);
		  ballCount = 0;
		  strikeCount = 0;
	  }
	  //home run
	  else if(distance >= 250) {
		  for(i = 0; i < 3; i++) {
			  if(bases[i]) {
				  scoreUpdate(1);
				  bases[i] = false;
			  }
		  }
		  scoreUpdate(1);
	  }
	  gameEnded();
  }
  
  public boolean isTopOfInning() {
	  
	  return isTopOfInning;
  }
  
  public boolean runnerOnBase(int which) {
	  
	  if(bases[which - 1]) {
		  return true;
	  }
	  else {
		  return false;
	  }
  }
  
  public void strike(boolean swung) {
	  
	  if(swung == true) {
		  outs++;
		  countReset();
	  }
	  else if(swung == false) {
		  strikeCount += 1;
	  }
	  if(strikeCount == MAX_STRIKES) {
		  outs++;
		  countReset();
	  }
	  if(outs == MAX_OUTS) {
		  if(gameEnded()) {
		  }
		  else {
			  switchTeam();
		  }
	  }
  }
  
  public int whichInning() {
	  if(inning > maxInnings) {
		  inning = maxInnings + 1;
	  }
	  return inning;
  }
  //resets the strike and ball count after a hit, strike-out, or an out
  private void countReset() {
	  strikeCount = 0;
	  ballCount = 0;
  }
  //updates the points for team 0 and team 1
  private void scoreUpdate(int score) {
	  
	  if(isTopOfInning()) {
		  scoreZero += score;
	  }
	  else {
		  scoreOne += score;
	  }
  }
  //moves runners when there is a walk
  private void shiftRunnersForWalk() {
	  
	  if(bases[0]) {
		  if(bases[1]) {
			  if(bases[2]) {
				  scoreUpdate(1);
				  bases[2] = false;
			  }
			  bases[2] = true;
			  bases[1] = false;
		  }
		  bases[1] = true;
		  bases[0] = false;
	  }
	  bases[0] = true;
	  
  }
  //Moving runners for a hit
  private void shiftRunners(int howMany) {
	  //single
	  if(howMany == 1) {
		  if(bases[2]) {
			  scoreUpdate(1);
			  bases[2] = false;
		  }
		  if(bases[1]) {
			  bases[2] = true;
			  bases[1] = false;
		  }
		  if(bases[0]) {
			  bases[1] = true;
		  }
		  bases[0] = true;
	  }
	  //double
	  else if(howMany == 2) {
		  if(bases[2]) {
			  scoreUpdate(1);
			  bases[2] = false;
		  }
		  if(bases[1]) {
			  scoreUpdate(1);
			  bases[1] = false;
		  }
		  if(bases[0]) {
			  bases[2] = true;
			  bases[0] = false;
		  }
		  bases[1] = true;
	  }
	  //triple
	  else if(howMany == 3) {
		if(bases[2]) {
			scoreUpdate(1);
			bases[2] = false;
		}
		if(bases[1]) {
			scoreUpdate(1);
			bases[1] = false;
		}
		if(bases[0]) {
			scoreUpdate(1);
			bases[0] = false;
		}
		bases[2] = true;
	  }
  }
  //Switches to top or bottom and resets and increments inning
  private void switchTeam() {
	  
	  isTopOfInning = !isTopOfInning;
	  outs = 0;
	  strikeCount = 0;
	  ballCount = 0;
	  for(i = 0; i < 3; i++) {
		  bases[i] = false;
	  }
	  if(isTopOfInning) {
		  inning++;
	  }
	  gameEnded();
  }
  
//Note that this code will not compile until you have put in stubs for all
 // the required methods.
 

 
 // The methods below are provided for you and you should not modify them.
 // The compile errors will go away after you have written stubs for the
 // rest of the API methods.
 /**
  * Returns a three-character string representing the players on base, in the
  * order first, second, and third, where 'X' indicates a player is present and
  * 'o' indicates no player. For example, the string "XXo" means that there are
  * players on first and second but not on third.
  * 
  * @return three-character string showing players on base
  */
 public String getBases()
 {
   return (runnerOnBase(1) ? "X" : "o") + (runnerOnBase(2) ? "X" : "o")
       + (runnerOnBase(3) ? "X" : "o");
 }

 /**
  * Returns a one-line string representation of the current game state. The
  * format is:
  * 
  * <pre>
  *    ooo Inning:1 (T) Score:0-0 Balls:0 Strikes:0 Outs:0
  * </pre>
  * 
  * The first three characters represent the players on base as returned by the
  * <code>getBases()</code> method. The 'T' after the inning number indicates
  * it's the top of the inning, and a 'B' would indicate the bottom. The score always
  * shows team 0 first.
  * 
  * @return one-line string representation of the game state
  */
 public String toString()
 {
   String bases = getBases();
   String topOrBottom = (isTopOfInning() ? "T" : "B");
   String fmt = "%s Inning:%d (%s) Score:%d-%d Balls:%d Strikes:%d Outs:%d";
   return String.format(fmt, bases, whichInning(), topOrBottom, getTeam0Score(),
       getTeam1Score(), getBallCount(), getCalledStrikes(), getCurrentOuts());
 }


}